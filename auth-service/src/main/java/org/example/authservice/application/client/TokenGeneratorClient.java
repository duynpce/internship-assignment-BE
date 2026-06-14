package org.example.authservice.application.client;

import org.example.authservice.application.command.AuthTokenCommand;

public interface TokenGeneratorClient {

    AuthTokenCommand generate(String username);
    long getExpiresInSeconds();
    boolean isRefreshTokenExpired(String token);
    boolean isAccessTokenExpired(String token);
    void validateAccessToken(String token);
    void validateRefreshToken(String token);
    String extractUsernameFromAccessToken(String accessToken);
    String extractUsernameFromRefreshToken(String refreshToken);
}
