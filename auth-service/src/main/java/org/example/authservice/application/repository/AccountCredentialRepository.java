package org.example.authservice.application.repository;

import org.example.authservice.domain.model.AccountCredential;

import java.util.Optional;
import java.util.UUID;

public interface AccountCredentialRepository {
    AccountCredential save(AccountCredential domain);

    Optional<AccountCredential> findById(UUID id);
    AccountCredential findByIdWithRolesAndPermissions(UUID id);
    AccountCredential findByKeycloakIdWithRolesAndPermissions(UUID keycloakId);
  Optional<AccountCredential> findByUsername(String username);
    Optional<AccountCredential> findByEmail(String email);


    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    void delete(UUID id);

}
