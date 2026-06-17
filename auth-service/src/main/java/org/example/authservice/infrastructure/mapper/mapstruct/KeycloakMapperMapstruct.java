package org.example.authservice.infrastructure.mapper.mapstruct;

import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface KeycloakMapperMapstruct {

    @Mapping(target = "refreshToken", source = "response.refreshToken")
    KeycloakTokenCommand toKeycloakSession(KeycloakTokenResponse response, String username, UUID userId);
}