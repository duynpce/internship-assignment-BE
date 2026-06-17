package org.example.authservice.infrastructure.mapper.adapter;

import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.LoginCommand;
import org.example.authservice.application.mapper.AuthTokenMapper;
import org.example.authservice.domain.model.AuthToken;
import org.example.authservice.infrastructure.mapper.mapstruct.AuthMapperMapstruct;
import org.example.authservice.infrastructure.web.dto.LoginRequest;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.example.authservice.infrastructure.web.entity.AuthTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthMapperAdapter implements AuthTokenMapper {

    private final AuthMapperMapstruct mapstructMapper;

    // Spring will automatically inject the MapStruct implementation here
    public AuthMapperAdapter(AuthMapperMapstruct mapstructMapper) {
        this.mapstructMapper = mapstructMapper;
    }

    @Override
    public LoginCommand toCommand(LoginRequest request) {
        if (request == null) {
            return null;
        }
        return mapstructMapper.toCommand(request);
    }

    @Override
    public TokenResponse toDto(AuthTokenCommand authTokenCommand) {
        if (authTokenCommand == null) {
            return null;
        }
        return mapstructMapper.toDto(authTokenCommand);
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
}