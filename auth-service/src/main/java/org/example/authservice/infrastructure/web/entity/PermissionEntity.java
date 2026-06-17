package org.example.authservice.infrastructure.web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.authservice.domain.constant.Action;
import org.example.authservice.domain.constant.Resource;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "permissions",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_permission_resource_action",
        columnNames = {"resource", "action"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "resource", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private Resource resource;

    @Column(name = "action", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Action action;


    public String toAuthority() {
        return resource.name() + ":" + action.name();
    }





}
