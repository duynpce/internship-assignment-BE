package org.example.authservice.domain.model;

import org.example.authservice.domain.valueobject.Email;
import org.example.authservice.domain.valueobject.PhoneNumber;

import java.util.Set;

public class AccountCredential {
    private String username;
    private String password;
    private Set<Role> roles;
    private Email email;
    private PhoneNumber phoneNumber;

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

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumber changePhoneNumber(String phoneNumber){
        return new PhoneNumber(phoneNumber);
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Email changeEmail(String email){
        return new Email(email);
    }
}
