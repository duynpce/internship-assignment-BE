package org.example.authservice.application.usecase;

import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.domain.exception.UnauthorizedException;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.KeycloakSessionCommand;

public class RefreshTokenUseCase {

    private final KeycloakClient keycloakClient;
    private final TokenGeneratorClient tokenGeneratorClient;

    public RefreshTokenUseCase(KeycloakClient keycloakClient, TokenGeneratorClient tokenGeneratorClient) {
        this.keycloakClient = keycloakClient;
        this.tokenGeneratorClient = tokenGeneratorClient;
    }

    public AuthTokenCommand refresh(String keycloakRefreshToken) {
        KeycloakSessionCommand session = keycloakClient.refresh(keycloakRefreshToken);

        if(keycloakRefreshToken.isEmpty()) {
            throw new UnauthorizedException("session expired or not logged in");
        }

        if (session == null) {
            throw new UnauthorizedException("session expired or not logged in");
        }

        return tokenGeneratorClient.generate(session.username());
    }
}