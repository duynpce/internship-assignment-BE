package org.example.authservice.infrastructure.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.KeycloakSessionCommand;
import org.example.authservice.infrastructure.config.KeycloakProperties;
import org.example.authservice.infrastructure.keycloak.client.KeycloakHttpClient;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.example.authservice.infrastructure.mapper.KeycloakMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakAdapter implements KeycloakClient {

    private final KeycloakHttpClient keycloakHttpClient;
    private final KeycloakMapper keycloakMapper;
    private final KeycloakProperties props;
    private final TokenGeneratorClient tokenGeneratorClient;

    @Override
    public void login(String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username",username);
        form.add("password",password);
        keycloakHttpClient.login(form, "user");
    }

    @Override
    public KeycloakSessionCommand refresh(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type",    "refresh_token");
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("refresh_token", refreshToken);

        KeycloakTokenResponse response = keycloakHttpClient.token(props.getRealm(), form);
        String username = tokenGeneratorClient.extractUsernameFromAccessToken(response.getAccessToken());
        return keycloakMapper.toKeycloakSession(response, username);
    }

}