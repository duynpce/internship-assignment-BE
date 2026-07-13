package org.example.authservice.infrastructure.mapper;

import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.application.mapper.KeycloakMapper;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface KeycloakMapperMapstruct extends KeycloakMapper {

    /** Maps a Keycloak token response to a session command, using email as the principal identifier. */
    @Mapping(target = "refreshToken", source = "response.refreshToken")
    KeycloakTokenCommand toKeycloakSession(KeycloakTokenResponse response, String email, UUID userId);
}