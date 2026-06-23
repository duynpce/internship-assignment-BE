package org.example.authservice.application.repository;

import org.example.authservice.domain.model.AccountCredential;

import java.util.UUID;

public interface AccountCredentialRepository {
    AccountCredential save(AccountCredential domain);

    AccountCredential findById(UUID id);

    AccountCredential findByIdWithRolesAndPermissions(UUID id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    void delete(UUID id);

}
