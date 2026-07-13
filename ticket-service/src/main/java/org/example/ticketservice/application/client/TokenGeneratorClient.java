package org.example.ticketservice.application.client;

import java.util.UUID;

public interface TokenGeneratorClient {

    UUID extractUserIdFromAccessToken(String accessToken);
}
