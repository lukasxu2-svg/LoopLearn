package com.example.saastest.modules.auth.controller;

import com.example.saastest.modules.auth.dto.request.LoginRequestBody;
import com.example.saastest.modules.auth.dto.request.RegisterRequestBody;
import com.example.saastest.modules.auth.dto.response.LoginResponseBody;
import com.example.saastest.modules.auth.dto.response.RegisterResponseBody;
import com.example.saastest.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponseBody login(@Valid @RequestBody LoginRequestBody body) {
        return service.login(body);
    }

    @PostMapping("/register")
    public RegisterResponseBody register(@Valid @RequestBody RegisterRequestBody body) {
        return service.register(body);
    }
}
