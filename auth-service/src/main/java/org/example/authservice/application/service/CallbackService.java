package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakLocalClient;
import org.example.authservice.application.client.KeycloakRemoteClient;
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
import org.example.authservice.infrastructure.config.JwtProperties;
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

    private final KeycloakRemoteClient keycloakRemoteClient;
    private final KeycloakLocalClient keycloakLocalClient;
    private final TokenGeneratorClient tokenGeneratorClient;
    private final AuthTokenRepository authTokenRepository;
    private final AccountCredentialRepository accountCredentialRepository;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public AuthTokenCommand remoteCallback(CallbackCommand command) {
        log.info("Remote OAuth2 callback received, exchanging authorization code for tokens");

        KeycloakTokenCommand keycloakSession = keycloakRemoteClient.exchangeCode(command.code());
        return buildAndSaveToken(keycloakSession);
    }

    @Override
    @Transactional
    public AuthTokenCommand localCallback(CallbackCommand command) {
        log.info("Local OAuth2 callback received, exchanging authorization code for tokens");

        KeycloakTokenCommand keycloakSession = keycloakLocalClient.exchangeCode(command.code());
        return buildAndSaveToken(keycloakSession);
    }

    private AuthTokenCommand buildAndSaveToken(KeycloakTokenCommand keycloakSession) {
        UUID userId = keycloakSession.userId();
        AccountCredential accountCredential = accountCredentialRepository.findByIdWithRolesAndPermissions(userId);
        Set<String> permissions = accountCredential.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::toAuthority)
                .collect(Collectors.toSet());



        AuthTokenCommand authToken = tokenGeneratorClient.generate(
                accountCredential.getUsername(),
                userId,
                permissions
        );

        AuthToken authTokenRecord = new AuthToken(
                userId,
                authToken.refreshToken(),
                keycloakSession.refreshToken(),
                Instant.now().plus(jwtProperties.getRefreshExpirationDay(), ChronoUnit.DAYS)
        );

        authTokenRepository.save(authTokenRecord);

        log.info("Callback successful for userId: {}", userId);
        return authToken;
    }

}
