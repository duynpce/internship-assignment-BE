package org.example.authservice.infrastructure.mapper.mapstruct;

import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface KeycloakMapperMapstruct {

    /** Local callback – email is null (local accounts already exist in account_credentials). */
    @Mapping(target = "refreshToken", source = "response.refreshToken")
    @Mapping(target = "email", ignore = true)
    KeycloakTokenCommand toKeycloakSession(KeycloakTokenResponse response, String username, UUID userId);

    /** Remote callback – email extracted from the Keycloak access token. */
    @Mapping(target = "refreshToken", source = "response.refreshToken")
    KeycloakTokenCommand toKeycloakSessionWithEmail(KeycloakTokenResponse response, String username, UUID userId, String email);
}