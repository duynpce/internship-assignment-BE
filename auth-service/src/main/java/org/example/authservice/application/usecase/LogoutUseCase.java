package org.example.authservice.application.usecase;

import java.util.UUID;

public interface LogoutUseCase {
    void logout(String authRefreshToken);
}