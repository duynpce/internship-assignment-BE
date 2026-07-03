package org.example.authservice.application.command;

public record AuthTokenCommand(
     String accessToken,
     String refreshToken,
     String tokenType,
     long expiresIn
 )
{}
