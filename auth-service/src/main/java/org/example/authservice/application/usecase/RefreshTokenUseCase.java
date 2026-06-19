package org.example.authservice.application.usecase;

import org.example.authservice.application.command.AuthTokenCommand;

public interface RefreshTokenUseCase {
    AuthTokenCommand remoteRefresh(String authRefreshToken);
    AuthTokenCommand localRefresh(String authRefreshToken);
}