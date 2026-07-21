package com.example.saastest.modules.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.refresh.timer}")
    private Duration refreshTimer;

    public String generateToken(Long benutzerId) {

        return Jwts.builder()
                .subject(benutzerId.toString())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + refreshTimer.toMillis())
                )
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
