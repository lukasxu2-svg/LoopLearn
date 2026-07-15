package com.example.saastest.modules.auth.dto.response;

public record RegisterResponseBody(
        String email,
        String token,
        Long expiresIn
) {
}
