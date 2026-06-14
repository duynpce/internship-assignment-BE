package org.example.authservice.infrastructure.config;

import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.usecase.LoginUseCase;
import org.example.authservice.application.usecase.RefreshTokenUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public LoginUseCase loginUseCase(KeycloakClient keycloakClient, TokenGeneratorClient tokenGeneratorClient) {
        return new LoginUseCase(keycloakClient, tokenGeneratorClient);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(KeycloakClient keycloakClient, TokenGeneratorClient tokenGeneratorClient) {
        return new RefreshTokenUseCase(keycloakClient, tokenGeneratorClient);
    }
}
