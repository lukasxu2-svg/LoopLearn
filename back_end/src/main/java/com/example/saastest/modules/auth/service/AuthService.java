package com.example.saastest.modules.auth.service;

import com.example.saastest.modules.auth.dto.request.LoginRequestBody;
import com.example.saastest.modules.auth.dto.request.RegisterRequestBody;
import com.example.saastest.modules.auth.dto.response.LoginResponseBody;
import com.example.saastest.modules.auth.dto.response.RegisterResponseBody;
import com.example.saastest.modules.benutzer.dto.UserDto;
import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import com.example.saastest.modules.plan.enums.PlanType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthService {

    @Value("${jwt.token.refresh.timer}")
    private Duration refreshTimer;
    private final BenutzerRepository benutzerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(BenutzerRepository benutzerRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.benutzerRepository = benutzerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseBody login(LoginRequestBody requestBody) {
        Benutzer benutzer = benutzerRepository.findByEmail(requestBody.email())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(requestBody.password(), benutzer.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtService.generateToken(benutzer.getId());

        return new LoginResponseBody(
                new UserDto(benutzer.getId(), benutzer.getEmail(), benutzer.getFirstname(), benutzer.getLastname()),
                token,
                "Bearer",
                refreshTimer.toSeconds());
    }

    public RegisterResponseBody register(RegisterRequestBody requestBody) {
        if (benutzerRepository.findByEmail(requestBody.email()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        String hashedPassword = passwordEncoder.encode(requestBody.password());
        Benutzer benutzer = new Benutzer(requestBody.firstname(), requestBody.lastname(), requestBody.email(),
                hashedPassword, PlanType.NONE);

        benutzer = benutzerRepository.save(benutzer);

        String token = jwtService.generateToken(benutzer.getId());

        return new RegisterResponseBody(
                requestBody.email(),
                token,
                refreshTimer.toSeconds());
    }
}
