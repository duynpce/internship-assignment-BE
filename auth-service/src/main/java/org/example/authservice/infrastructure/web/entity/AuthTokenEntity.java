package org.example.authservice.infrastructure.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auth_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "auth_refresh_token", nullable = false, length = 2048)
    private String authRefreshToken;

    @Column(name = "keycloak_refresh_token", length = 2048)
    private String keycloakRefreshToken;

    @Column(name = "session_expire_at", nullable = false)
    private Instant sessionExpireAt;
}