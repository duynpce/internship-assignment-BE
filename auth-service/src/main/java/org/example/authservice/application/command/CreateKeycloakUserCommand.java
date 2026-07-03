package org.example.authservice.application.command;

public record CreateKeycloakUserCommand(
    String username,
    String password,
    String email
) {
}
