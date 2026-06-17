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
import org.example.authservice.infrastructure.config.JwtProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    public AuthTokenCommand refresh(String authRefreshToken) {
        log.info("Refresh Token: {}", authRefreshToken);
        if (authRefreshToken == null || authRefreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token must not be empty");
        }

        tokenGeneratorClient.validateRefreshToken(authRefreshToken);
        UUID userId = tokenGeneratorClient.extractUserIdFromRefreshToken(authRefreshToken);
        String username = tokenGeneratorClient.extractUsernameFromRefreshToken(authRefreshToken);

        AuthToken stored = authTokenRepository.findByUserId(userId);
        if (stored == null || !stored.getAuthRefreshToken().equals(authRefreshToken)) {
            throw new UnauthorizedException("Refresh token has been revoked");
        }

        KeycloakTokenCommand keycloakSession = keycloakClient.refresh(stored.getKeycloakRefreshToken(), username);

        AuthTokenCommand newAuthToken = tokenGeneratorClient.generate(username, userId);

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
