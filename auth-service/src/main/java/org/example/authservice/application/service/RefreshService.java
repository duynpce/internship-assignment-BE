package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.application.repository.AuthTokenRepository;
import org.example.authservice.application.usecase.RefreshTokenUseCase;
import org.example.authservice.domain.exception.UnauthorizedException;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.infrastructure.prop.JwtProperties;
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

    private final KeycloakClient keycloakClient;
    private final TokenGeneratorClient tokenGeneratorClient;
    private final AuthTokenRepository authTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public AuthTokenCommand remoteRefresh(String authRefreshToken) {
        validateToken(authRefreshToken);
        AuthToken stored = loadToken(authRefreshToken);
        String username = tokenGeneratorClient.extractUsernameFromRefreshToken(authRefreshToken);
        UUID userId     = tokenGeneratorClient.extractUserIdFromRefreshToken(authRefreshToken);

        if (stored == null || !stored.getAuthRefreshToken().equals(authRefreshToken)) {
            throw new UnauthorizedException("You are not logged in");
        }

        KeycloakTokenCommand keycloakSession = keycloakClient.refresh(stored.getKeycloakRefreshToken(), username);
        return saveNewToken(userId, username, keycloakSession, authRefreshToken);
    }

    @Override
    @Transactional
    public AuthTokenCommand localRefresh(String authRefreshToken) {
        validateToken(authRefreshToken);
        AuthToken stored = loadToken(authRefreshToken);
        String username = tokenGeneratorClient.extractUsernameFromRefreshToken(authRefreshToken);
        UUID userId     = tokenGeneratorClient.extractUserIdFromRefreshToken(authRefreshToken);

        if (stored == null || !stored.getAuthRefreshToken().equals(authRefreshToken)) {
            throw new UnauthorizedException("You are not logged in");
        }

        return saveNewToken(userId, username, null, authRefreshToken);
    }


    private void validateToken(String authRefreshToken) {
        if (authRefreshToken == null || authRefreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token must not be empty");
        }

        tokenGeneratorClient.validateRefreshToken(authRefreshToken);
    }

    private AuthToken loadToken(String authRefreshToken) {
        UUID userId = tokenGeneratorClient.extractUserIdFromRefreshToken(authRefreshToken);

        return authTokenRepository.findByUserId(userId);
    }



    private AuthTokenCommand saveNewToken(UUID userId, String username, KeycloakTokenCommand keycloakSession, String authRefreshToken) {
        Set<String> roles       = tokenGeneratorClient.extractRolesFromRefreshToken(authRefreshToken);
        Set<String> permissions = tokenGeneratorClient.extractPermissionsFromRefreshToken(authRefreshToken);

        AuthTokenCommand newAuthToken = tokenGeneratorClient.generate(username, userId, roles, permissions);

        AuthToken updatedRecord = new AuthToken(
                userId,
                newAuthToken.refreshToken(),
                keycloakSession == null ? null : keycloakSession.refreshToken(),
                Instant.now().plus(jwtProperties.getRefreshExpirationDay(), ChronoUnit.DAYS)
        );

        authTokenRepository.save(updatedRecord);
        return newAuthToken;
    }
}
