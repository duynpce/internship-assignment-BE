package org.example.authservice.domain.model;

import org.example.authservice.domain.valueobject.Email;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RemoteAccountCredential {
    private UUID id;
    private Email email;
    private Set<Role> roles = new HashSet<>();

    public RemoteAccountCredential() {
    }

    public RemoteAccountCredential(UUID id, Email email) {
        this.id = id;
        this.email = email;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Email getEmail() { return email; }
    public void setEmail(Email email) { this.email = email; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public void addRole(Role role) {
        if (this.roles == null) this.roles = new HashSet<>();
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if (this.roles != null) this.roles.remove(role);
    }
}


