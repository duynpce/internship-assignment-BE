package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.repository.AuthTokenRepository;
import org.example.authservice.application.usecase.LogoutUseCase;
import org.example.authservice.domain.exception.UnauthorizedException;
import org.example.authservice.domain.model.AuthToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutUseCase {

    private final KeycloakClient keycloakClient;
    private final AuthTokenRepository authTokenRepository;
    private final TokenGeneratorClient tokenGeneratorClient;

    @Override
    @Transactional
    public void remoteLogout(String authRefreshToken) {
        AuthToken storedToken = validateAndLoad(authRefreshToken);
        keycloakClient.logout(storedToken.getKeycloakRefreshToken());
        authTokenRepository.deleteByAuthRefreshToken(storedToken.getAuthRefreshToken());
        log.info("Remote logout successful for userId: {}", storedToken.getUserId());
    }

    @Override
    @Transactional
    public void localLogout(String authRefreshToken) {
        AuthToken storedToken = validateAndLoad(authRefreshToken);
        authTokenRepository.deleteByAuthRefreshToken(storedToken.getAuthRefreshToken());
        log.info("Local logout successful for userId: {}", storedToken.getUserId());
    }

    private AuthToken validateAndLoad(String authRefreshToken) {
        if (authRefreshToken == null) {
            throw new UnauthorizedException("you not logged in");
        }
        UUID userId = tokenGeneratorClient.extractUserIdFromRefreshToken(authRefreshToken);
        log.info("Logout requested for userId: {}", userId);

        AuthToken storedToken = authTokenRepository.findByUserId(userId);

        if (storedToken == null || !storedToken.getAuthRefreshToken().equals(authRefreshToken)) {
            throw new UnauthorizedException("you are not logged in");
        }

        return storedToken;
    }
}