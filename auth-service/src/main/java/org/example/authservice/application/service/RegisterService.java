package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.client.UserClient;
import org.example.authservice.application.command.CreateCredentialAccountCommand;
import org.example.authservice.application.command.RegisterCommand;
import org.example.authservice.application.mapper.AuthMapper;
import org.example.authservice.application.repository.AccountCredentialRepository;
import org.example.authservice.application.repository.RoleRepository;
import org.example.authservice.application.usecase.RegisterUseCase;
import org.example.authservice.domain.exception.ConflictException;
import org.example.authservice.domain.exception.NotFoundException;
import org.example.authservice.domain.model.AccountCredential;
import org.example.authservice.infrastructure.web.dto.CreateAccountRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {
    private final UserClient userClient;
    private final AccountCredentialRepository accountCredentialRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private static final String DEFAULT_ROLE = "CUSTOMER";

    @Override
    @Transactional
    public void register(RegisterCommand command) {
        CreateCredentialAccountCommand createCredentialAccountCommand = authMapper.toCreateCredentialAccountCommand(command);
        if(accountCredentialRepository.existsByUsername(createCredentialAccountCommand.username())){
            throw new ConflictException("Username already exists");
        }

        if(accountCredentialRepository.existsByEmail(createCredentialAccountCommand.email())){
            throw new ConflictException("Email already exists");
        }

        AccountCredential accountCredential = authMapper.toDomain(createCredentialAccountCommand);
        accountCredential.setPassword(passwordEncoder.encode(accountCredential.getPassword()));
        accountCredential.getRoles().add(roleRepository.findByName(DEFAULT_ROLE).orElseThrow(() -> new NotFoundException("Default role not found")));
        AccountCredential saved = accountCredentialRepository.save(accountCredential);


        CreateAccountRequest createAccountRequest = authMapper.toCreateAccountRequest(command);
        createAccountRequest.setUserId(saved.getId());
        userClient.createAccount(createAccountRequest);


    }

}
