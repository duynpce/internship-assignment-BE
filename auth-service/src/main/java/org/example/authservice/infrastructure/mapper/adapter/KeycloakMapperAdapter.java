package org.example.authservice.infrastructure.mapper.adapter;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.application.mapper.KeycloakMapper;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.example.authservice.infrastructure.mapper.mapstruct.KeycloakMapperMapstruct;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KeycloakMapperAdapter implements KeycloakMapper {
    private final KeycloakMapperMapstruct keycloakMapperMapstruct;

    @Override
    public KeycloakTokenCommand toKeycloakSession(KeycloakTokenResponse response, String username, UUID userId) {
        return keycloakMapperMapstruct.toKeycloakSession(response,username ,userId);
    }
}
