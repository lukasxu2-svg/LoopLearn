package com.example.saastest.modules.auth.service;

import com.example.saastest.modules.benutzer.entity.Benutzer;
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

    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + refreshTimer.toMillis())
                )
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
