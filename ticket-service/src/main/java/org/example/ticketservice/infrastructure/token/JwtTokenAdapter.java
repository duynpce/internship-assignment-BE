package org.example.ticketservice.infrastructure.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.ticketservice.application.client.TokenGeneratorClient;
import org.example.ticketservice.domain.exception.UnauthorizedException;
import org.example.ticketservice.infrastructure.prop.JwtProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements TokenGeneratorClient {

    private static final String CLAIM_USER_ID = "userId";

    private final JwtProperties jwtProperties;

    @Override
    public UUID extractUserIdFromAccessToken(String accessToken) {
        String raw = extractClaim(accessToken, claims -> claims.get(CLAIM_USER_ID, String.class));
        return UUID.fromString(raw);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getAccessKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Access token has expired.");
        } catch (JwtException e) {
            throw new UnauthorizedException("Access token is invalid.");
        }
    }

    private SecretKey getAccessKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getAccessSecret()));
    }
}
