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
import org.example.authservice.application.repository.RemoteAccountCredentialRepository;
import org.example.authservice.application.repository.RoleRepository;
import org.example.authservice.application.usecase.CallbackUseCase;
import org.example.authservice.domain.exception.NotFoundException;
import org.example.authservice.domain.model.AccountCredential;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.domain.model.Permission;
import org.example.authservice.domain.model.RemoteAccountCredential;
import org.example.authservice.domain.model.Role;
import org.example.authservice.domain.valueobject.Email;
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

    private final KeycloakRemoteClient keycloakRemoteClient;
    private final KeycloakLocalClient keycloakLocalClient;
    private final TokenGeneratorClient tokenGeneratorClient;
    private final AuthTokenRepository authTokenRepository;
    private final AccountCredentialRepository accountCredentialRepository;
    private final RemoteAccountCredentialRepository remoteAccountCredentialRepository;
    private final RoleRepository roleRepository;
    private final JwtProperties jwtProperties;

    // ── Remote (Google, GitHub, …) ────────────────────────────────────────────

    @Override
    @Transactional
    public AuthTokenCommand remoteCallback(CallbackCommand command) {
        log.info("Remote OAuth2 callback received, exchanging authorization code for tokens");

        KeycloakTokenCommand keycloakSession = keycloakRemoteClient.exchangeCode(command.code());
        UUID userId = keycloakSession.userId();

        // Find or auto-provision remote account
        RemoteAccountCredential account = remoteAccountCredentialRepository
                .findByIdWithRoles(userId)
                .orElseGet(() -> provisionRemoteAccount(userId, keycloakSession.email()));

        Set<String> roles = extractRoleNames(account.getRoles());
        Set<String> permissions = extractPermissions(account.getRoles());

        return buildAndSaveAuthToken(keycloakSession.username(), userId, keycloakSession.refreshToken(), roles, permissions);
    }

    // ── Local (user-federation Keycloak) ─────────────────────────────────────

    @Override
    @Transactional
    public AuthTokenCommand localCallback(CallbackCommand command) {
        log.info("Local OAuth2 callback received, exchanging authorization code for tokens");

        KeycloakTokenCommand keycloakSession = keycloakLocalClient.exchangeCode(command.code());
        UUID userId = keycloakSession.userId();

        AccountCredential account = accountCredentialRepository.findByIdWithRolesAndPermissions(userId);
        Set<String> roles = extractRoleNames(account.getRoles());
        Set<String> permissions = extractPermissions(account.getRoles());

        return buildAndSaveAuthToken(keycloakSession.username(), userId, keycloakSession.refreshToken(), roles, permissions);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Creates a brand-new {@link RemoteAccountCredential} for a first-time remote login,
     * assigning the default CUSTOMER role.
     */
    private RemoteAccountCredential provisionRemoteAccount(UUID userId, String rawEmail) {
        log.info("Provisioning new remote account for userId: {}", userId);

        Role defaultRole = roleRepository.getDefaultRole()
                .orElseThrow(() -> new NotFoundException("Default role '" + RoleRepository.DEFAULT_ROLE_NAME + "' not found"));

        RemoteAccountCredential newAccount = new RemoteAccountCredential(userId, new Email(rawEmail));
        newAccount.addRole(defaultRole);

        RemoteAccountCredential saved = remoteAccountCredentialRepository.save(newAccount);
        log.info("Remote account provisioned for userId: {}", userId);
        return saved;
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
