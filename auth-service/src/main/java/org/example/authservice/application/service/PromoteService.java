package org.example.authservice.application.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.repository.AccountCredentialRepository;
import org.example.authservice.application.repository.RoleRepository;
import org.example.authservice.application.usecase.PromoteUseCase;
import org.example.authservice.domain.exception.NotFoundException;
import org.example.authservice.domain.model.AccountCredential;
import org.example.authservice.domain.model.Role;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromoteService implements PromoteUseCase {

    private final AccountCredentialRepository accountCredentialRepository;
    private final RoleRepository roleRepository;

    @Override
    public void promoteToContributor(String userId) {
        AccountCredential account = accountCredentialRepository
                .findByIdWithRolesAndPermissions(UUID.fromString(userId));

        Role contributorRole = roleRepository
                .findByName("CONTRIBUTOR")
                .orElseThrow(() -> new NotFoundException("Role CONTRIBUTOR not found"));

        account.getRoles().add(contributorRole);

        accountCredentialRepository.save(account);
    }
}