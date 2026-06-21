package org.example.userservice.application.usecase;

import org.example.userservice.application.command.CreateAccountCommand;

public interface AccountUseCase {
    void createAccount(CreateAccountCommand command);
}