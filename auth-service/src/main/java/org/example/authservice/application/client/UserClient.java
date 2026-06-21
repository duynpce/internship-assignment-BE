package org.example.authservice.application.client;

import org.example.authservice.application.command.CreateAccountCommand;
import org.example.authservice.infrastructure.web.dto.CreateAccountRequest;

public interface UserClient {
    void createAccount(CreateAccountRequest request);
}
