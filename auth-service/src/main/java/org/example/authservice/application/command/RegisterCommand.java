package org.example.authservice.application.command;

import org.example.authservice.domain.constant.Gender;

public record RegisterCommand(
        String username,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Gender gender,
        String email
) {
}