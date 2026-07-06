package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.LoginCommand;
import org.example.authservice.application.repository.AccountCredentialRepository;
import org.example.authservice.application.usecase.LoginUseCase;
import org.example.authservice.domain.exception.UnauthorizedException;
import org.example.authservice.domain.model.AccountCredential;
import org.example.authservice.domain.model.Permission;
import org.example.authservice.domain.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase{
    private final AccountCredentialRepository accountCredentialRepository;
    private final TokenGeneratorClient tokenGeneratorClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthTokenCommand login(LoginCommand loginCommand) {
        AccountCredential accountCredential = accountCredentialRepository.findByUsername(loginCommand.username());

        if(!passwordEncoder.matches(loginCommand.password(), accountCredential.getPassword())){
            throw new UnauthorizedException("invalid username or password");
        }

        Set<String> roles = accountCredential.extractRoleNames();
        Set<String> permissions = accountCredential.extractPermissions();


        return tokenGeneratorClient.generate( accountCredential.getUsername(),accountCredential.getId(),roles, permissions);
    }



}
