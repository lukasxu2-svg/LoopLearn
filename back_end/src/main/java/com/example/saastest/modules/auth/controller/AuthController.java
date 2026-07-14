package com.example.saastest.modules.auth.controller;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final BenutzerRepository benutzerRepository;

    public AuthController(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody Benutzer benutzer) {
        benutzerRepository.findByName(benutzer.getName());
    }

    @PostMapping("/register")
    public ResponseEntity<Benutzer> register(@Valid @RequestBody Benutzer benutzer) {
        Benutzer saved = benutzerRepository.save(benutzer);
        return ResponseEntity.ok(saved);
    }
}
