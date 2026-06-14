package org.example.authservice.infrastructure.mapper;

import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.infrastructure.web.dto.LoginRequest;
import org.example.authservice.infrastructure.web.dto.RegisterRequest;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.example.authservice.application.command.LoginCommand;
import org.example.authservice.application.command.RegisterCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    LoginCommand toCommand(LoginRequest request);
    RegisterCommand toCommand(RegisterRequest request);
    TokenResponse toDto(AuthTokenCommand authTokenCommand);
}