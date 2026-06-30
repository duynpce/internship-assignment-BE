package org.example.authservice.infrastructure.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
    name = "remote_account_credentials",
    uniqueConstraints = @UniqueConstraint(name = "uq_remote_account_email", columnNames = "email")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemoteAccountCredentialEntity {

    /** Keycloak sub UUID – set by caller, never auto-generated. */
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "remote_account_roles",
        joinColumns        = @JoinColumn(name = "account_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id",    referencedColumnName = "id")
    )
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();

    public void addRole(RoleEntity role)    { this.roles.add(role); }
    public void removeRole(RoleEntity role) { this.roles.remove(role); }
}

