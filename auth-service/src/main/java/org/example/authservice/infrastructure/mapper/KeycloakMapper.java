// infrastructure/mapper/KeycloakMapper.java
package org.example.authservice.infrastructure.mapper;

import org.example.authservice.application.command.KeycloakSessionCommand;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KeycloakMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "refreshToken", source = "response.refreshToken")
    KeycloakSessionCommand toKeycloakSession(KeycloakTokenResponse response, String username);
}