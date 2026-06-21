package org.example.authservice.application.command;

import org.example.authservice.domain.constant.Gender;

public record CreateAccountCommand(
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Gender gender
) {
}