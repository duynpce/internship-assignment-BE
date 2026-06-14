package org.example.authservice.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
    @NotBlank String username,
    @Email @NotBlank String email,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String password,
    @NotBlank String phoneNumber,
    @NotBlank String address
) {}
