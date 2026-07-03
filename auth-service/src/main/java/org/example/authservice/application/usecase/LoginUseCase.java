package org.example.authservice.application.usecase;

import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.LoginCommand;

public interface LoginUseCase {
    AuthTokenCommand  login(LoginCommand loginCommand);
}
