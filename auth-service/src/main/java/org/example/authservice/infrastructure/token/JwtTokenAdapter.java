package org.example.authservice.infrastructure.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.domain.exception.UnauthorizedException;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.infrastructure.config.JwtProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtTokenAdapter implements TokenGeneratorClient {

    private final JwtProperties jwtProperties;

    public JwtTokenAdapter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // ── Interface methods ────────────────────────────────────────────────────

    @Override
    public AuthTokenCommand generate(String username) {
        String accessToken = generateAccessToken(username);
        String refreshToken = generateRefreshToken(username);
        return new AuthTokenCommand(accessToken, refreshToken, "Bearer", getExpiresInSeconds());
    }

    @Override
    public long getExpiresInSeconds() {
        return TimeUnit.MINUTES.toSeconds(jwtProperties.getAccessExpirationMinutes());
    }

    @Override
    public boolean isAccessTokenExpired(String token) {
        return extractExpiration(token, getAccessKey()).before(new Date());
    }

    @Override
    public boolean isRefreshTokenExpired(String token) {
        return extractExpiration(token, getRefreshKey()).before(new Date());
    }

    @Override
    public void validateAccessToken(String token) {
        validate(token, getAccessKey(), "Access token");
    }

    @Override
    public void validateRefreshToken(String token) {
        validate(token, getRefreshKey(), "Refresh token");
    }

    // ── Access token helpers ─────────────────────────────────────────────────

    @Override
    public String extractUsernameFromAccessToken(String token) {
        return extractClaim(token, Claims::getSubject, getAccessKey());
    }

    @Override
    public String extractUsernameFromRefreshToken(String token) {
        return extractClaim(token, Claims::getSubject, getRefreshKey());
    }

    // ── Private: generate ────────────────────────────────────────────────────

    private String generateAccessToken(String username) {
        long expirationMs = TimeUnit.MINUTES.toMillis(jwtProperties.getAccessExpirationMinutes());
        return buildToken(username, getAccessKey(), expirationMs);
    }

    private String generateRefreshToken(String username) {
        long expirationMs = TimeUnit.DAYS.toMillis(jwtProperties.getRefreshExpirationDay());
        return buildToken(username, getRefreshKey(), expirationMs);
    }

    private String buildToken(String username, SecretKey key, long expirationMs) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key)
                .compact();
    }

    // ── Private: validate & extract ──────────────────────────────────────────

    private void validate(String token, SecretKey key, String tokenLabel) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(tokenLabel + " has expired");
        } catch (JwtException e) {
            throw new UnauthorizedException(tokenLabel + " is invalid");
        }
    }

    private Date extractExpiration(String token, SecretKey key) {
        return extractClaim(token, Claims::getExpiration, key);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, SecretKey key) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    // ── Private: keys ────────────────────────────────────────────────────────

    private SecretKey getAccessKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getAccessSecret()));
    }

    private SecretKey getRefreshKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getRefreshSecret()));
    }
}