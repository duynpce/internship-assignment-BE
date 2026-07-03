package org.example.authservice.domain.model;

import org.example.authservice.domain.constant.AccountStatus;
import org.example.authservice.domain.valueobject.Email;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class AccountCredential {
    private UUID id ;
    private UUID KeycloakId;
    private String username;
    private String password;
    private Set<Role> roles =  new HashSet<>();
    private Email email;
    private AccountStatus status = AccountStatus.ACTIVE;
    private Instant createdAt =  Instant.now();
    private Instant updatedAt;


    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?\\\\|~`])\\S{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getKeycloakId() {
        return KeycloakId;
    }

    public void setKeycloakId(UUID keycloakId) {
        KeycloakId = keycloakId;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if(username == null || username.length() < 8){
            throw new IllegalArgumentException("username length should be at least 8 characters");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validatePassword(password);
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

    void validatePassword(String password)
    {
        if(password == null || password.isEmpty())
        {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        if(!pattern.matcher(password).matches())
        {
            throw new IllegalArgumentException("Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        if(this.password != null &&  this.password.equals(password))
        {
            throw new IllegalArgumentException("cannot change to your current password");
        }
    }
}
