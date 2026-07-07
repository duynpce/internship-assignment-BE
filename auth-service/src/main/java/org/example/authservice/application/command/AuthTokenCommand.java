package org.example.authservice.application.command;

import java.util.Set;

public record AuthTokenCommand(
     String accessToken,
     String refreshToken,
     String tokenType,
     long expiresIn,
     Set<String> roles
 )
{}
