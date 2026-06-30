package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakLocalClient;
import org.example.authservice.application.client.KeycloakRemoteClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.application.repository.AccountCredentialRepository;
import org.example.authservice.application.repository.AuthTokenRepository;
import org.example.authservice.application.usecase.RefreshTokenUseCase;
import org.example.authservice.domain.exception.UnauthorizedException;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.infrastructure.config.JwtProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshService implements RefreshTokenUseCase {

    private final KeycloakRemoteClient keycloakRemoteClient;
    private final KeycloakLocalClient keycloakLocalClient;
    private final TokenGeneratorClient tokenGeneratorClient;
    private final AuthTokenRepository authTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public AuthTokenCommand remoteRefresh(String authRefreshToken) {
        AuthToken stored = validateAndLoad(authRefreshToken);
        String username = tokenGeneratorClient.extractUsernameFromRefreshToken(authRefreshToken);
        UUID userId     = tokenGeneratorClient.extractUserIdFromRefreshToken(authRefreshToken);

        KeycloakTokenCommand keycloakSession = keycloakRemoteClient.refresh(stored.getKeycloakRefreshToken(), username);
        return saveNewToken(userId, username, keycloakSession, authRefreshToken);
    }

    @Override
    @Transactional
    public AuthTokenCommand localRefresh(String authRefreshToken) {
        AuthToken stored = validateAndLoad(authRefreshToken);
        String username = tokenGeneratorClient.extractUsernameFromRefreshToken(authRefreshToken);
        UUID userId     = tokenGeneratorClient.extractUserIdFromRefreshToken(authRefreshToken);

        KeycloakTokenCommand keycloakSession = keycloakLocalClient.refresh(stored.getKeycloakRefreshToken(), username);
        return saveNewToken(userId, username, keycloakSession, authRefreshToken);
    }

    private AuthToken validateAndLoad(String authRefreshToken) {
        if (authRefreshToken == null || authRefreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token must not be empty");
        }
        tokenGeneratorClient.validateRefreshToken(authRefreshToken);
        UUID userId = tokenGeneratorClient.extractUserIdFromRefreshToken(authRefreshToken);

        AuthToken stored = authTokenRepository.findByUserId(userId);

        if (stored == null || !stored.getAuthRefreshToken().equals(authRefreshToken)) {
            throw new UnauthorizedException("you are not logged in");
        }
        return stored;
    }

    private AuthTokenCommand saveNewToken(UUID userId, String username, KeycloakTokenCommand keycloakSession, String authRefreshToken) {
        Set<String> permissions = tokenGeneratorClient.extractPermissionsFromRefreshToken(authRefreshToken);

        AuthTokenCommand newAuthToken = tokenGeneratorClient.generate(username, userId, permissions);

        AuthToken updatedRecord = new AuthToken(
                userId,
                newAuthToken.refreshToken(),
                keycloakSession.refreshToken(),
                Instant.now().plus(jwtProperties.getRefreshExpirationDay(), ChronoUnit.DAYS)
        );

        authTokenRepository.save(updatedRecord);
        return newAuthToken;
    }
}
