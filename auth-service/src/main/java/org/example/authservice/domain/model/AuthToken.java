package org.example.authservice.domain.model;


import java.time.Instant;
import java.util.UUID;

public class AuthToken {
    private UUID userId;
    private String authRefreshToken;
    private String keycloakRefreshToken;
    private Instant sessionExpireAt;

    public AuthToken() {
    }

    public AuthToken(UUID userId, String authRefreshToken, String keycloakRefreshToken, Instant sessionExpireAt) {
        this.userId = userId;
        this.authRefreshToken = authRefreshToken;
        this.keycloakRefreshToken = keycloakRefreshToken;
        this.sessionExpireAt = sessionExpireAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAuthRefreshToken() {
        return authRefreshToken;
    }

    public void setAuthRefreshToken(String authRefreshToken) {
        this.authRefreshToken = authRefreshToken;
    }

    public String getKeycloakRefreshToken() {
        return keycloakRefreshToken;
    }

    public void setKeycloakRefreshToken(String keycloakRefreshToken) {
        this.keycloakRefreshToken = keycloakRefreshToken;
    }

    public Instant getSessionExpireAt() {
        return sessionExpireAt;
    }

    public void setSessionExpireAt(Instant sessionExpireAt) {
        this.sessionExpireAt = sessionExpireAt;
    }




}
