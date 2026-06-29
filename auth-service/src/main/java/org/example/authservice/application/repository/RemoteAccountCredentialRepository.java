package org.example.authservice.application.repository;

import org.example.authservice.domain.model.RemoteAccountCredential;

import java.util.Optional;
import java.util.UUID;

public interface RemoteAccountCredentialRepository {

    Optional<RemoteAccountCredential> findById(UUID id);

    /** Eager-loads roles + permissions via EntityGraph. */
    Optional<RemoteAccountCredential> findByIdWithRoles(UUID id);

    RemoteAccountCredential save(RemoteAccountCredential domain);
}

