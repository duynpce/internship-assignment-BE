package org.example.authservice.infrastructure.mapper.mapstruct;

import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.infrastructure.web.dto.LoginRequest;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.example.authservice.application.command.LoginCommand;
import org.example.authservice.infrastructure.web.entity.AuthTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapperMapstruct {
    LoginCommand toCommand(LoginRequest request);
    TokenResponse toDto(AuthTokenCommand authTokenCommand);
    AuthTokenEntity toEntity(AuthToken domain);
    AuthToken toDomain(AuthTokenEntity entity);
}

