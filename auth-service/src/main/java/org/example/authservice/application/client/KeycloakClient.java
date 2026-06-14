// domain/client/KeycloakClient.java
package org.example.authservice.application.client;

import org.example.authservice.application.command.KeycloakSessionCommand;

public interface KeycloakClient {
    void login(String username, String password);
    KeycloakSessionCommand refresh(String refreshToken);
}