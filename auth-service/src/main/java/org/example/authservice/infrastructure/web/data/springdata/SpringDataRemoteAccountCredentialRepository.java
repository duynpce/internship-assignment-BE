package org.example.authservice.infrastructure.web.data.springdata;

import org.example.authservice.infrastructure.web.entity.RemoteAccountCredentialEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataRemoteAccountCredentialRepository
        extends JpaRepository<RemoteAccountCredentialEntity, UUID> {

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<RemoteAccountCredentialEntity> findWithRolesById(UUID id);
}

