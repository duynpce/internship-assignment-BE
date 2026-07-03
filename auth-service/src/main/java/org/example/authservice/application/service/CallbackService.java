package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.CallbackCommand;
import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.application.repository.AccountCredentialRepository;
import org.example.authservice.application.repository.AuthTokenRepository;
import org.example.authservice.application.usecase.CallbackUseCase;
import org.example.authservice.domain.model.AccountCredential;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.domain.model.Permission;
import org.example.authservice.domain.model.Role;
import org.example.authservice.infrastructure.prop.JwtProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallbackService implements CallbackUseCase {

    private final KeycloakClient keycloakClient;
    private final TokenGeneratorClient tokenGeneratorClient;
    private final AuthTokenRepository authTokenRepository;
    private final AccountCredentialRepository accountCredentialRepository;
    private final JwtProperties jwtProperties;


    @Override
    @Transactional
    public AuthTokenCommand remoteCallback(CallbackCommand command) {
        log.info("Remote OAuth2 callback received, exchanging authorization code for tokens");

        KeycloakTokenCommand keycloakSession = keycloakClient.exchangeCode(command.code());
        UUID keycloakUserId = keycloakSession.userId();
        String username = keycloakSession.username();

        AccountCredential accountCredential = accountCredentialRepository.findByUsername(username);

        //if keycloak id not exist then save it
        if(accountCredential.getKeycloakId() == null){
            accountCredential.setKeycloakId(keycloakUserId);
            accountCredentialRepository.save(accountCredential);
        }


        Set<String> roles = extractRoleNames(accountCredential.getRoles());
        Set<String> permissions = extractPermissions(accountCredential.getRoles());

        return buildAndSaveAuthToken(keycloakSession.username(), accountCredential.getId(), keycloakSession.refreshToken(), roles, permissions);
    }


    private Set<String> extractRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    private Set<String> extractPermissions(Set<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::toAuthority)
                .collect(Collectors.toSet());
    }

    private AuthTokenCommand buildAndSaveAuthToken(String username, UUID userId,
                                                    String keycloakRefreshToken,
                                                    Set<String> roles,
                                                    Set<String> permissions) {
        AuthTokenCommand authToken = tokenGeneratorClient.generate(username, userId, roles, permissions);

        AuthToken authTokenRecord = new AuthToken(
                userId,
                authToken.refreshToken(),
                keycloakRefreshToken,
                Instant.now().plus(jwtProperties.getRefreshExpirationDay(), ChronoUnit.DAYS)
        );

        authTokenRepository.save(authTokenRecord);
        log.info("Callback successful for userId: {}", userId);
        return authToken;
    }
}
