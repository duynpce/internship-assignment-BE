package org.example.authservice.infrastructure.config;

import org.example.authservice.domain.exception.ExternalServiceException;
import org.example.authservice.infrastructure.keycloak.client.KeycloakHttpClient;
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
    public KeycloakHttpClient keycloakHttpClient(KeycloakProperties props) {
        WebClient webClient = WebClient.builder()
                .baseUrl(props.getServerUrl())
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

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(KeycloakHttpClient.class);
    }
}
