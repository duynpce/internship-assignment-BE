package org.example.authservice.application.command;

import java.util.UUID;
public record UpdateKeycloakUserCommand(
        UUID keycloakUserId,
        String password,
        String email
) {
}
