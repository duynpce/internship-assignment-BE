package org.example.authservice.application.command;

public record CreateCredentialAccountCommand(
        String username,
        String password,
        String email
) {
}