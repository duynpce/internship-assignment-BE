package org.example.authservice.application.command;

import lombok.Data;

public record KeycloakSessionCommand(String username, String refreshToken) {

}
