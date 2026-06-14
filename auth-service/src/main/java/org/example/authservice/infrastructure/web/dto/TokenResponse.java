package org.example.authservice.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record TokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_in") long expiresIn
) {}
