package org.example.authservice.infrastructure.web.data.springdata;

import org.example.authservice.infrastructure.web.entity.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAuthTokenRepository extends JpaRepository<AuthTokenEntity, UUID> {

    void deleteByAuthRefreshToken(String authRefreshToken);

    boolean existsByAuthRefreshToken(String authRefreshToken);
}