package org.example.authservice.infrastructure.config;

import org.example.authservice.domain.exception.ExternalServiceException;
import org.example.authservice.infrastructure.keycloak.client.KeycloakLocalHttpClient;
import org.example.authservice.infrastructure.keycloak.client.KeycloakRemoteHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class HttpClientConfig {

    @Bean
    public KeycloakRemoteHttpClient keycloakRemoteHttpClient(KeycloakProperties props) {
        WebClient webClient = buildWebClient(props.getServerRemoteUrl());
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build()
                .createClient(KeycloakRemoteHttpClient.class);
    }

    @Bean
    public KeycloakLocalHttpClient keycloakLocalHttpClient(KeycloakProperties props) {
        WebClient webClient = buildWebClient(props.getServerLocalUrl());
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build()
                .createClient(KeycloakLocalHttpClient.class);
    }

    private WebClient buildWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty(clientResponse.statusCode().toString())
                                .flatMap(errorBody -> Mono.error(
                                        new ExternalServiceException(
                                                "Keycloak Error: " + errorBody,
                                                HttpStatus.resolve(clientResponse.statusCode().value())
                                        )
                                ))
                )
                .build();
    }
}
