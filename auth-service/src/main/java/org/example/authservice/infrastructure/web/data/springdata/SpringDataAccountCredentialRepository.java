package org.example.authservice.infrastructure.web.data.springdata;

import org.example.authservice.infrastructure.web.entity.AccountCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAccountCredentialRepository extends JpaRepository<AccountCredentialEntity, UUID> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}