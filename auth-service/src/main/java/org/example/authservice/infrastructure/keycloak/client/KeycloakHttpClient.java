package org.example.authservice.infrastructure.keycloak.client;

import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakUserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;       // ← Spring, not Swagger
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface KeycloakHttpClient {

    @PostExchange(value = "/realms/{realm}/custom-auth/login", contentType = "application/x-www-form-urlencoded")
    void login(@RequestBody MultiValueMap<String, String> params, @PathVariable String realm);  // ← thêm @RequestBody

    @PostExchange(value = "/realms/{realm}/protocol/openid-connect/token", contentType = "application/x-www-form-urlencoded")
    KeycloakTokenResponse token(
            @PathVariable String realm,
            @RequestBody MultiValueMap<String, String> form
    );

    @PostExchange("/admin/realms/{realm}/users")
    ResponseEntity<Void> createUser(
            @PathVariable String realm,
            @RequestBody KeycloakUserRepresentation user
    );

    @DeleteExchange("/admin/realms/{realm}/users/{userId}")
    void deleteUser(
            @PathVariable String realm,
            @PathVariable String userId
    );
}