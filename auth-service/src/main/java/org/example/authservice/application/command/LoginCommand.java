package org.example.authservice.application.command;

public record LoginCommand(
        String username, String password
) {
}
