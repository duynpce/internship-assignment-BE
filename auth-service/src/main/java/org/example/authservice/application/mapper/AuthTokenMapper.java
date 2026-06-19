package org.example.authservice.application.mapper;

import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.example.authservice.infrastructure.web.entity.AuthTokenEntity;

public interface AuthTokenMapper {
    public AuthTokenEntity toEntity(AuthToken domain);
    public AuthToken toDomain(AuthTokenEntity domain);
    LoginCommand toCommand(LoginRequest request);
    TokenResponse toDto(AuthTokenCommand authTokenCommand);
}
