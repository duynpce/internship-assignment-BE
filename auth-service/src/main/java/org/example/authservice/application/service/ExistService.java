package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.repository.AccountCredentialRepository;
import org.example.authservice.application.usecase.ExistUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExistService implements ExistUseCase {

    private final AccountCredentialRepository accountCredentialRepository;

    @Override
    public boolean existsByUsername(String username) {
        return accountCredentialRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountCredentialRepository.existsByEmail(email);
    }
}

