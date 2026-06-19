package org.example.authservice.domain.model;

import org.example.authservice.domain.constant.AccountStatus;
import org.example.authservice.domain.valueobject.Email;

import java.time.Instant;
import java.util.Set;

public class AccountCredential {
    private String username;
    private String password;
    private Set<Role> roles;
    private Email email;
    private AccountStatus status;
    private Instant createdAt =  Instant.now();
    private Instant updatedAt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Email changeEmail(String email){
        return new Email(email);
    }
}
