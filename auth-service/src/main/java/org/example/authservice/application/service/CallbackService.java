package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.CallbackCommand;
import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.application.repository.AuthTokenRepository;
import org.example.authservice.application.usecase.CallbackUseCase;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.infrastructure.config.JwtProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallbackService implements CallbackUseCase {

    private final KeycloakClient keycloakClient;
    private final TokenGeneratorClient tokenGeneratorClient;
    private final AuthTokenRepository authTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public AuthTokenCommand callback(CallbackCommand command) {
        log.info("OAuth2 callback received, exchanging authorization code for tokens");

        KeycloakTokenCommand keycloakSession = keycloakClient.exchangeCode(command.code());

        AuthTokenCommand authToken = tokenGeneratorClient.generate(
                keycloakSession.username(),
                keycloakSession.userId()
        );

        AuthToken authTokenRecord = new AuthToken(
                keycloakSession.userId(),
                authToken.refreshToken(),
                keycloakSession.refreshToken(),
                Instant.now().plus(jwtProperties.getRefreshExpirationDay(), ChronoUnit.DAYS)
        );

        authTokenRepository.save(authTokenRecord);

        log.info("OAuth2 callback successful for userId: {}", keycloakSession.userId());
        return authToken;
    }
}
