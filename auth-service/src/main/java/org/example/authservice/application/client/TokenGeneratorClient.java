package org.example.authservice.application.client;

import org.example.authservice.application.command.AuthTokenCommand;

import java.util.Set;
import java.util.UUID;

public interface TokenGeneratorClient {

    AuthTokenCommand generate(String username, UUID userId, Set<String> permissions);
    long getExpiresInSeconds();
    boolean isRefreshTokenExpired(String token);
    boolean isAccessTokenExpired(String token);
    void validateAccessToken(String token);
    void validateRefreshToken(String token);

    String extractUsernameFromAccessToken(String accessToken);
    String extractUsernameFromRefreshToken(String refreshToken);
    UUID extractUserIdFromAccessToken(String accessToken);
    UUID extractUserIdFromRefreshToken(String refreshToken);
    Set<String> extractRolesFromRefreshToken(String refreshToken);
    Set<String> extractPermissionsFromRefreshToken(String refreshToken);

    UUID extractUserIdFromLocalKeycloakAccessToken(String keycloakAccessToken);
    UUID extractUserIdFromRemoteKeycloakAccessToken(String keycloakAccessToken);
    String extractUsernameFromLocalKeycloakAccessToken(String keycloakAccessToken);
    String extractUsernameFromRemoteKeycloakAccessToken(String keycloakAccessToken);
    String extractEmailFromRemoteKeycloakAccessToken(String keycloakAccessToken);

}