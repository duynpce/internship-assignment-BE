package org.example.authservice.infrastructure.keycloak.client;

import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakUserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface KeycloakHttpClient {

    @PostExchange(value = "/realms/{realm}/protocol/openid-connect/token", contentType = "application/x-www-form-urlencoded")
    KeycloakTokenResponse login(
            @RequestBody MultiValueMap<String, String> form,
            @PathVariable String realm
    );

    @PostExchange(value = "/realms/{realm}/protocol/openid-connect/logout", contentType = "application/x-www-form-urlencoded")
    void logout(
            @RequestBody MultiValueMap<String, String> form,
            @PathVariable String realm
    );

    @PostExchange(value = "/realms/{realm}/protocol/openid-connect/token", contentType = "application/x-www-form-urlencoded")
    KeycloakTokenResponse token(
            @PathVariable String realm,
            @RequestBody MultiValueMap<String, String> form
    );
}