package org.example.authservice.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String username,
    @NotBlank String password
) {}
