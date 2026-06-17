// domain/client/KeycloakClient.java
package org.example.authservice.application.client;

import org.example.authservice.application.command.KeycloakTokenCommand;

public interface KeycloakClient {
    KeycloakTokenCommand login(String username, String password);
    KeycloakTokenCommand refresh(String keycloakRefreshToken, String username);
    void logout(String refreshToken);
    KeycloakTokenCommand exchangeCode(String code);

}