package org.example.authservice.infrastructure.mapper.adapter;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.CreateCredentialAccountCommand;
import org.example.authservice.application.command.RegisterCommand;
import org.example.authservice.application.mapper.AuthMapper;
import org.example.authservice.domain.model.AccountCredential;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.domain.model.Role;
import org.example.authservice.infrastructure.mapper.mapstruct.AuthMapperMapstruct;
import org.example.authservice.infrastructure.web.dto.CreateAccountRequest;
import org.example.authservice.infrastructure.web.dto.RegisterRequest;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.example.authservice.infrastructure.web.entity.AccountCredentialEntity;
import org.example.authservice.infrastructure.web.entity.AuthTokenEntity;
import org.example.authservice.infrastructure.web.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMapperAdapter implements AuthMapper {

    private final AuthMapperMapstruct mapstructMapper;


    @Override
    public TokenResponse toDto(AuthTokenCommand authTokenCommand) {
        if (authTokenCommand == null) {
            return null;
        }
        return mapstructMapper.toDto(authTokenCommand);
    }

    @Override
    public RegisterCommand toCommand(RegisterRequest request) {
        if(request == null) {
            return null;
        }
        return mapstructMapper.toCommand(request);
    }

    @Override
    public CreateAccountRequest toCreateAccountRequest(RegisterCommand command) {
        if (command == null) {
            return null;
        }
        return mapstructMapper.toCreateAccountRequest(command);
    }

    @Override
    public CreateCredentialAccountCommand toCreateCredentialAccountCommand(RegisterCommand command) {
        if (command == null) {
            return null;
        }
        return mapstructMapper.toCreateCredentialAccountCommand(command);
    }

    @Override
    public AccountCredentialEntity toEntity(AccountCredential domain) {
        if (domain == null) {
            return null;
        }
        return mapstructMapper.toEntity(domain);
    }

    @Override
    public AccountCredential toDomain(CreateCredentialAccountCommand domain) {
        if (domain == null) {
            return null;
        }
        return mapstructMapper.toDomain(domain);
    }

    @Override
    public AccountCredential toDomain(AccountCredentialEntity entity) {
        if (entity == null) {
            return null;
        }
        return mapstructMapper.toDomain(entity);
    }

    @Override
    public AuthTokenEntity toEntity(AuthToken domain) {
        if (domain == null) {
            return null;
        }
        return mapstructMapper.toEntity(domain);
    }

    @Override
    public AuthToken toDomain(AuthTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        return mapstructMapper.toDomain(entity);
    }

    @Override
    public Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return mapstructMapper.toDomain(entity);
    }

}