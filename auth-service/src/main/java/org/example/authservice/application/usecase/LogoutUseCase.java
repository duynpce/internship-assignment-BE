package org.example.authservice.application.usecase;

import java.util.UUID;

public interface LogoutUseCase {
    void remoteLogout(String authRefreshToken);
    void localLogout(String authRefreshToken);
}