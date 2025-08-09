package com.JCC.LeAtcoderAPI.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey signingKey;

    public JwtService(@Value("${jwt.secret}") String jwtSecret) {
        if (jwtSecret == null) {
            throw new IllegalArgumentException("jwt secret not set");
        }
        signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private String createTokenBySubAndExpiry(String sub, long seconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(now.plusSeconds(seconds)))
                .signWith(signingKey)
                .compact();
    }

    public String createTempToken(String googleId) {
        return createTokenBySubAndExpiry(googleId, 60);
    }

    public String createAccessToken(String userId) {
        return createTokenBySubAndExpiry(userId, 60 * 60 * 72);
    }

    public String createRefreshToken(String userId) {
        return createTokenBySubAndExpiry(userId, 60 * 60 * 24 * 30);
    }

    public String extractToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            // Handle cases where the token is invalid or expired
            return null; // or throw a specific exception
        }
    }
}
