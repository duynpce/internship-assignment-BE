package org.example.authservice.application.command;


import org.example.authservice.domain.exception.ValidationException;

public record LoginCommand(
    String username,
    String password
) {
    void validate() {
        if(username == null || password == null) {
            throw new ValidationException("Username and password must not be null");
        }
    }
}
