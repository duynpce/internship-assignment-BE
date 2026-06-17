package org.example.authservice.application.usecase;

import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.CallbackCommand;

public interface CallbackUseCase {
    AuthTokenCommand callback(CallbackCommand command);
}
