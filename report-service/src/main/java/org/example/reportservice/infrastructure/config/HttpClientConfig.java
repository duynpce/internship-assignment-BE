package org.example.reportservice.infrastructure.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

import io.netty.channel.ChannelOption;
import org.example.reportservice.domain.exception.ExternalServiceException;
import org.example.reportservice.infrastructure.prop.AppProperties;
import org.example.reportservice.infrastructure.user.httpclient.UserHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class HttpClientConfig {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(10);

    @Bean
    public UserHttpClient userHttpClient(AppProperties props) {
        return createHttpClient(props.getUserServiceUrl(), UserHttpClient.class);
    }

    private <T> T createHttpClient(String baseUrl, Class<T> clientClass) {
        WebClient webClient = buildWebClient(baseUrl);
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(clientClass);
    }

    private WebClient buildWebClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) CONNECT_TIMEOUT.toMillis())
                .responseTimeout(RESPONSE_TIMEOUT);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // ── Forward the Bearer token from the current security context ──────────
                .filter((request, next) ->
                        ReactiveSecurityContextHolder.getContext()
                                .map(SecurityContext::getAuthentication)
                                .filter(auth -> auth instanceof JwtAuthenticationToken)
                                .cast(JwtAuthenticationToken.class)
                                .map(auth -> auth.getToken().getTokenValue())
                                .defaultIfEmpty("")
                                .flatMap(token -> {
                                    if (token.isBlank()) {
                                        return next.exchange(request);
                                    }
                                    return next.exchange(
                                            // Mutate the outgoing request to add Authorization header
                                            org.springframework.web.reactive.function.client.ClientRequest
                                                    .from(request)
                                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                                    .build()
                                    );
                                })
                )
                // ─────────────────────────────────────────────────────────────────────
                .defaultStatusHandler(HttpStatusCode::isError, this::handleErrorResponse)
                .build();
    }

    private Mono<? extends Throwable> handleErrorResponse(ClientResponse clientResponse) {
        HttpStatus status = Optional.ofNullable(HttpStatus.resolve(clientResponse.statusCode().value()))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        return clientResponse.bodyToMono(String.class)
                .defaultIfEmpty(status.getReasonPhrase())
                .flatMap(errorBody -> {
                    String message = extractMessage(errorBody);
                    return Mono.error(new ExternalServiceException(message, status));
                });
    }

    private String extractMessage(String errorBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(errorBody);
            JsonNode messageNode = root.path("message");
            return messageNode.isMissingNode() ? errorBody : messageNode.asText();
        } catch (Exception e) {
            return errorBody;
        }
    }
}