package org.example.authservice.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Role {
    private UUID id;
    private String name;
    private Set<Permission> permissions = new HashSet<>();

    public Role(UUID id, String name, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public Role() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        if (this.permissions == null) {
            this.permissions = new HashSet<>();
        }
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        if (this.permissions != null) {
            this.permissions.remove(permission);
        }
    }


}