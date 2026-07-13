package org.example.authservice.application.client;

import org.example.authservice.application.command.AuthTokenCommand;

import java.util.Set;
import java.util.UUID;

public interface TokenGeneratorClient {

    AuthTokenCommand generate(String username, UUID userId, Set<String> roles, Set<String> permissions);
    long getExpiresInSeconds();
    boolean isRefreshTokenExpired(String token);
    boolean isAccessTokenExpired(String token);
    void validateAccessToken(String token);
    void validateRefreshToken(String token);

    String extractEmailFromAccessToken(String accessToken);
    String extractEmailFromRefreshToken(String refreshToken);
    UUID extractUserIdFromAccessToken(String accessToken);
    UUID extractUserIdFromRefreshToken(String refreshToken);
    Set<String> extractRolesFromAccessToken(String accessToken);
    Set<String> extractRolesFromRefreshToken(String refreshToken);
    Set<String> extractPermissionsFromRefreshToken(String refreshToken);

    UUID extractUserIdFromKeycloakAccessToken(String keycloakAccessToken);
    String extractEmailFromKeycloakAccessToken(String keycloakAccessToken);

}