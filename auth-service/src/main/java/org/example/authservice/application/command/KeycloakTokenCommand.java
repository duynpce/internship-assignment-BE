package org.example.authservice.application.command;

import java.util.UUID;

public record KeycloakTokenCommand(String username,UUID userId, String refreshToken) {

}
