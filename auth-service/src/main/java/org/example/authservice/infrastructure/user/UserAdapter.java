package org.example.authservice.infrastructure.user;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.client.UserClient;
import org.example.authservice.infrastructure.user.httpclient.UserHttpClient;
import org.example.authservice.infrastructure.web.dto.CreateAccountRequest;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAdapter implements UserClient {
    private final UserHttpClient userHttpClient;

    @Override
    public void createAccount(CreateAccountRequest request) {
        userHttpClient.createAccount(request);
    }
}
