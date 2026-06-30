

package org.example.apigateway.infrastructure.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.apigateway.infrastructure.prop.AppProperties;
import org.example.apigateway.infrastructure.prop.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
@EnableWebFluxSecurity  // replaces @EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppProperties appProperties;
    private final JwtProperties jwtProperties;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(auth -> auth
                        .pathMatchers("/local/test-auth").authenticated()
                        .anyExchange().permitAll()
                )
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(resourceServerAuthenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
                        .authenticationEntryPoint(resourceServerAuthenticationEntryPoint())
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler((exchange, authentication) ->
                                Mono.fromRunnable(() ->
                                        exchange.getExchange().getResponse()
                                                .setStatusCode(HttpStatus.NO_CONTENT)
                                )
                        )
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(appProperties.getClientUri()));
        config.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        };
    }

    private ServerAuthenticationEntryPoint resourceServerAuthenticationEntryPoint() {
        return (exchange, ex) -> {
            String path = exchange.getRequest().getPath().value();
            log.error("Unauthorized access attempt on path: {}", path);

            exchange.getResponse().getHeaders().add("Content-Type", "application/json");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getAccessSecret()));
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}