package org.example.authservice.application.client;

import org.example.authservice.application.command.AuthTokenCommand;

import java.util.UUID;

public interface TokenGeneratorClient {

    AuthTokenCommand generate(String username, UUID userId);
    long getExpiresInSeconds();
    boolean isRefreshTokenExpired(String token);
    boolean isAccessTokenExpired(String token);
    void validateAccessToken(String token);
    void validateRefreshToken(String token);
    String extractUsernameFromAccessToken(String accessToken);
    String extractUsernameFromRefreshToken(String refreshToken);
    UUID extractUserIdFromAccessToken(String accessToken);
    UUID extractUserIdFromRefreshToken(String refreshToken);
    UUID extractUserIdFromKeycloakAccessToken(String accessToken);
    String extractUsernameFromKeycloakAccessToken(String keycloakAccessToken);

}