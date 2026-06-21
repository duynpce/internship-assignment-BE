package org.example.authservice.infrastructure.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.authservice.application.client.TokenGeneratorClient;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.domain.exception.UnauthorizedException;
import org.example.authservice.infrastructure.config.JwtProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class    JwtTokenAdapter implements TokenGeneratorClient {

    private static final String CLAIM_USER_ID = "userId";
    private final JwtProperties jwtProperties;
    private final JwtDecoder keycloakLocalDecoder;
    private final JwtDecoder keycloakRemoteDecoder;

    public JwtTokenAdapter(JwtProperties jwtProperties,
                           @Qualifier("keycloakLocalJwtDecoder") JwtDecoder keycloakLocalDecoder,
                           @Qualifier("keycloakRemoteJwtDecoder") JwtDecoder keycloakRemoteDecoder) {
        this.jwtProperties = jwtProperties;
        this.keycloakLocalDecoder = keycloakLocalDecoder;
        this.keycloakRemoteDecoder = keycloakRemoteDecoder;
    }


    // ── Interface methods ────────────────────────────────────────────────────

    @Override
    public AuthTokenCommand generate(String username, UUID userId) {
        String accessToken  = generateAccessToken(username, userId);
        String refreshToken = generateRefreshToken(username, userId);
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

    // ── Extract username ─────────────────────────────────────────────────────

    @Override
    public String extractUsernameFromAccessToken(String token) {
        return extractClaim(token, Claims::getSubject, getAccessKey());
    }

    @Override
    public String extractUsernameFromRefreshToken(String token) {
        return extractClaim(token, Claims::getSubject, getRefreshKey());
    }

    // ── Extract userId ───────────────────────────────────────────────────────

    @Override
    public UUID extractUserIdFromAccessToken(String token) {
        String raw = extractClaim(token, c -> c.get(CLAIM_USER_ID, String.class), getAccessKey());
        return UUID.fromString(raw);
    }

    @Override
    public UUID extractUserIdFromRefreshToken(String token) {
        String raw = extractClaim(token, c -> c.get(CLAIM_USER_ID, String.class), getRefreshKey());
        return UUID.fromString(raw);
    }

    @Override
    //user federation --> have prefix
    public UUID extractUserIdFromLocalKeycloakAccessToken(String keycloakAccessToken) {
        String sub = keycloakLocalDecoder.decode(keycloakAccessToken).getClaimAsString("sub");
        String userId = sub.contains(":") ? sub.substring(sub.lastIndexOf(":") + 1) : sub;
        return UUID.fromString(userId);
    }

    // user provider database --> no prefix
    @Override
    public UUID extractUserIdFromRemoteKeycloakAccessToken(String keycloakAccessToken) {
        return UUID.fromString(keycloakRemoteDecoder.decode(keycloakAccessToken).getClaimAsString("sub"));
    }

    @Override
    public String extractUsernameFromLocalKeycloakAccessToken(String keycloakAccessToken) {
        return keycloakLocalDecoder.decode(keycloakAccessToken).getClaimAsString("preferred_username");
    }

    @Override
    public String extractUsernameFromRemoteKeycloakAccessToken(String keycloakAccessToken) {
        return keycloakRemoteDecoder.decode(keycloakAccessToken).getClaimAsString("preferred_username");
    }


    // ── Private: generate ────────────────────────────────────────────────────

    private String generateAccessToken(String username, UUID userId) {
        long expirationMs = TimeUnit.MINUTES.toMillis(jwtProperties.getAccessExpirationMinutes());
        return buildToken(username, userId, getAccessKey(), expirationMs);
    }

    private String generateRefreshToken(String username, UUID userId) {
        long expirationMs = TimeUnit.DAYS.toMillis(jwtProperties.getRefreshExpirationDay());
        return buildToken(username, userId, getRefreshKey(), expirationMs);
    }

    private String buildToken(String username, UUID userId, SecretKey key, long expirationMs) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .claim(CLAIM_USER_ID, userId.toString())
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