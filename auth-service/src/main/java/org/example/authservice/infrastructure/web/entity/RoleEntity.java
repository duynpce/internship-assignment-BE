package org.example.authservice.infrastructure.web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * RBAC – Role (nhóm quyền).
 *
 * Một Role tập hợp nhiều Permission. Account được gán Role,
 * và thừa hưởng toàn bộ Permission của Role đó.
 *
 * Bảng: roles
 * Join:  role_permissions  (roles ↔ permissions)
 */
@Entity
@Table(
    name = "roles",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_role_name",
        columnNames = "name"
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns        = @JoinColumn(name = "role_id",       referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<PermissionEntity> permissions = new HashSet<>();



    public void addPermission(PermissionEntity permission) {
        this.permissions.add(permission);
    }

    public void removePermission(PermissionEntity permission) {
        this.permissions.remove(permission);
    }
}
