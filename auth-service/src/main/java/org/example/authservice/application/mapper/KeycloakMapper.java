// infrastructure/mapper/KeycloakMapper.java
package org.example.authservice.application.mapper;

import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;


public interface KeycloakMapper {

    KeycloakTokenCommand toKeycloakSession(KeycloakTokenResponse response, String username, UUID userId);
}