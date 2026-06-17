package org.example.authservice.infrastructure.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.infrastructure.config.KeycloakProperties;
import org.example.authservice.infrastructure.keycloak.client.KeycloakHttpClient;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.example.authservice.application.mapper.KeycloakMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakAdapter implements KeycloakClient {

    private final KeycloakHttpClient keycloakHttpClient;
    private final KeycloakMapper keycloakMapper;
    private final KeycloakProperties props;
    private final TokenGeneratorClient tokenGeneratorClient;

    @Override
    public KeycloakTokenCommand login(String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type",    "password");
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("username",      username);
        form.add("password",      password);

        KeycloakTokenResponse response = keycloakHttpClient.login(form, props.getRealm());
        UUID userId = tokenGeneratorClient.extractUserIdFromKeycloakAccessToken(response.getAccessToken());
        return keycloakMapper.toKeycloakSession(response, username, userId);
    }

    @Override
    public void logout(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("refresh_token", refreshToken);

        keycloakHttpClient.logout(form, props.getRealm());
    }

    @Override
    public KeycloakTokenCommand refresh(String keycloakRefreshToken, String username) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type",    "refresh_token");
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("refresh_token", keycloakRefreshToken);

        KeycloakTokenResponse response = keycloakHttpClient.token(props.getRealm(), form);
        UUID userId = tokenGeneratorClient.extractUserIdFromKeycloakAccessToken(response.getAccessToken());

        return keycloakMapper.toKeycloakSession(response, username, userId);
    }

    @Override
    public KeycloakTokenCommand exchangeCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type",    "authorization_code");
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("code",          code);
        form.add("redirect_uri",  props.getRedirectUri());

        log.info(props.getRedirectUri());
        log.info("Exchanging authorization code with Keycloak, realm: {}", props.getRealm());

        KeycloakTokenResponse response = keycloakHttpClient.token(props.getRealm(), form);

        UUID userId   = tokenGeneratorClient.extractUserIdFromKeycloakAccessToken(response.getAccessToken());
        String username = tokenGeneratorClient.extractUsernameFromKeycloakAccessToken(response.getAccessToken());

        return keycloakMapper.toKeycloakSession(response, username, userId);
    }
}