package org.example.authservice.infrastructure.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.client.KeycloakRemoteClient;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.KeycloakTokenCommand;
import org.example.authservice.infrastructure.config.KeycloakProperties;
import org.example.authservice.infrastructure.keycloak.httpclient.KeycloakRemoteHttpClient;
import org.example.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import org.example.authservice.application.mapper.KeycloakMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakRemoteAdapter implements KeycloakRemoteClient {

    private final KeycloakRemoteHttpClient keycloakRemoteHttpClient;
    private final KeycloakMapper keycloakMapper;
    private final KeycloakProperties props;
    private final TokenGeneratorClient tokenGeneratorClient;


    @Override
    public void logout(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("refresh_token", refreshToken);

        keycloakRemoteHttpClient.logout(form, props.getRealm());
    }

    @Override
    public KeycloakTokenCommand refresh(String keycloakRefreshToken, String username) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type",    "refresh_token");
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("refresh_token", keycloakRefreshToken);

        KeycloakTokenResponse response = keycloakRemoteHttpClient.token(props.getRealm(), form);
        UUID userId = tokenGeneratorClient.extractUserIdFromRemoteKeycloakAccessToken(response.getAccessToken());

        return keycloakMapper.toKeycloakSession(response, username, userId);
    }

    @Override
    public KeycloakTokenCommand exchangeCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type",    "authorization_code");
        form.add("client_id",     props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("code",          code);
        form.add("redirect_uri",  props.getRemoteRedirectUri());

        log.info(props.getRemoteRedirectUri());
        log.info("Exchanging authorization code with Keycloak, realm: {}", props.getRealm());

        KeycloakTokenResponse response = keycloakRemoteHttpClient.token(props.getRealm(), form);

        UUID userId   = tokenGeneratorClient.extractUserIdFromRemoteKeycloakAccessToken(response.getAccessToken());
        String username = tokenGeneratorClient.extractUsernameFromRemoteKeycloakAccessToken(response.getAccessToken());

        return keycloakMapper.toKeycloakSession(response, username, userId);
    }
}