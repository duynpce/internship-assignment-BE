package org.example.authservice.application.usecase;

import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.LoginCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginUseCase {

    private final KeycloakClient  keycloakClient;
    private final TokenGeneratorClient tokenGeneratorClient;
    private static final Logger log = LoggerFactory.getLogger(LoginUseCase.class);

    public LoginUseCase(KeycloakClient keycloakClient, TokenGeneratorClient tokenGeneratorClient) {
        this.keycloakClient = keycloakClient;
        this.tokenGeneratorClient = tokenGeneratorClient;
    }

        public AuthTokenCommand login (LoginCommand command){
            log.info("Login command received with username: " + command.username());
            keycloakClient.login(command.username(), command.password());
            return tokenGeneratorClient.generate(command.username());

        }
}
