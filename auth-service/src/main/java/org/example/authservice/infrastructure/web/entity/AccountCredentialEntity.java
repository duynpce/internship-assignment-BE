package org.example.authservice.infrastructure.web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.authservice.domain.constant.AccountStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
    name = "account_credentials",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_account_username", columnNames = "username"),
        @UniqueConstraint(name = "uq_account_email",    columnNames = "email")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCredentialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "account_roles",
        joinColumns        = @JoinColumn(name = "account_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id",    referencedColumnName = "id")
    )
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public void addRole(RoleEntity role) {
        this.roles.add(role);
    }

    public void removeRole(RoleEntity role) {
        this.roles.remove(role);
    }

    public Set<PermissionEntity> getAllPermissions() {
        Set<PermissionEntity> permissions = new HashSet<>();
        this.roles.forEach(role -> permissions.addAll(role.getPermissions()));
        return permissions;
    }
}
