package org.example.authservice.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CallbackRequest(
        @NotBlank(message = "Authorization code must not be blank")
        String code
) {}
