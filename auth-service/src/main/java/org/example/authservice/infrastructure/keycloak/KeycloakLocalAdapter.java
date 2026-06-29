package org.example.authservice.infrastructure.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakLocalClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.application.mapper.KeycloakMapper;
import org.example.authservice.infrastructure.config.KeycloakProperties;
import org.example.authservice.infrastructure.keycloak.httpclient.KeycloakLocalHttpClient;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakLocalAdapter implements KeycloakLocalClient {

    private final KeycloakLocalHttpClient keycloakLocalHttpClient;
    private final KeycloakMapper keycloakMapper;
    private final KeycloakProperties props;
    private final TokenGeneratorClient tokenGeneratorClient;

    @Override
    public void logout(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("refresh_token", refreshToken);
        keycloakLocalHttpClient.logout(form, props.getRealm());
    }

    @Override
    public KeycloakTokenCommand refresh(String keycloakRefreshToken, String username) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type",    "refresh_token");
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("refresh_token", keycloakRefreshToken);

        KeycloakTokenResponse response = keycloakLocalHttpClient.token(props.getRealm(), form);
        UUID userId = tokenGeneratorClient.extractUserIdFromLocalKeycloakAccessToken(response.getAccessToken());
        return keycloakMapper.toKeycloakSession(response, username, userId);
    }

    @Override
    public KeycloakTokenCommand exchangeCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type",    "authorization_code");
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("code",          code);
        form.add("redirect_uri",  props.getLocalRedirectUri());


        KeycloakTokenResponse response = keycloakLocalHttpClient.token(props.getRealm(), form);
        UUID userId     = tokenGeneratorClient.extractUserIdFromLocalKeycloakAccessToken(response.getAccessToken());
        String username = tokenGeneratorClient.extractUsernameFromLocalKeycloakAccessToken(response.getAccessToken());
        return keycloakMapper.toKeycloakSession(response, username, userId);
    }
}

