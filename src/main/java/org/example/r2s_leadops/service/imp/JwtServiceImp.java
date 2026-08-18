package org.example.r2s_leadops.service.imp;

import org.example.r2s_leadops.config.JwtProperties;
import org.example.r2s_leadops.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Sinh va giai ma JWT access token. Refresh token la 1 chuoi random doc lap
 * (khong phai JWT) vi ban chat cua no chi la "ma tra cuu" trong bang refresh_tokens,
 * giup thu hoi duoc ma khong can blacklist JWT.
 */
@Service
@RequiredArgsConstructor
public class JwtServiceImp {

    private final JwtProperties jwtProperties;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getAccessTokenTtlMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().getCode().name())
                .claim("full_name", user.getFullName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .id(UUID.randomUUID().toString())
                .signWith(signingKey())
                .compact();
    }

    public Claims parseAndValidate(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(Claims claims) {
        return UUID.fromString(claims.getSubject());
    }
    public String extractEmail(Claims claims){
        return claims.get("email", String.class);
    }
    public String extractRole(Claims claims){
        return claims.get("role", String.class);
    }

    /** Sinh chuoi random dung lam gia tri refresh token / reset-password token tra ve cho client. */
    public String generateOpaqueToken() {
        byte[] bytes = new byte[48];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public long getRefreshTokenTtlDays() {
        return jwtProperties.getRefreshTokenTtlDays();
    }

    public long getAccessTokenTtlSeconds() {
        return jwtProperties.getAccessTokenTtlMinutes() * 60;
    }
}

