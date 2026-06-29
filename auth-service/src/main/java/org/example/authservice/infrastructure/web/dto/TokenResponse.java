package org.example.authservice.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record TokenResponse(
    String accessToken,
    String tokenType,
    long expiresIn
) {}
