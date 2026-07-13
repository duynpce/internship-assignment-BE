package org.example.authservice.application.command;

import java.util.UUID;

/**
 * @param email nullable – populated only for remote (third-party) OAuth2 callbacks
 */
public record KeycloakTokenCommand( UUID userId, String refreshToken, String email) {
}
