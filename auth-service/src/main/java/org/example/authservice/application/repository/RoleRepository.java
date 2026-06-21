package org.example.authservice.application.repository;

import org.example.authservice.domain.model.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Optional<Role> findByName(String name);
}