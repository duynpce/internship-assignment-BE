package org.example.authservice.application.usecase;

import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.CreateCredentialAccountCommand;
import org.example.authservice.application.command.RegisterCommand;

public interface RegisterUseCase {
    void register(RegisterCommand command);
}
