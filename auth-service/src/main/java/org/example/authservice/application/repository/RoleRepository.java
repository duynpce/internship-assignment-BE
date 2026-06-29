package org.example.authservice.application.repository;

import org.example.authservice.domain.model.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
     static final String DEFAULT_ROLE_NAME = "CUSTOMER";
    Optional<Role> findByName(String name);
    default Optional<Role> getDefaultRole() {
        return findByName(DEFAULT_ROLE_NAME);
    }
}