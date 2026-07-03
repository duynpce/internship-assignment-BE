// infrastructure/mapper/KeycloakMapper.java
package org.example.authservice.application.mapper;

import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;


public interface KeycloakMapper {

    /** Local – email not carried. */
    KeycloakTokenCommand toKeycloakSession(KeycloakTokenResponse response, String username, UUID userId);

    /** Remote – email from Keycloak access-token claim. */
    KeycloakTokenCommand toKeycloakSessionWithEmail(KeycloakTokenResponse response, String username, UUID userId, String email);
}