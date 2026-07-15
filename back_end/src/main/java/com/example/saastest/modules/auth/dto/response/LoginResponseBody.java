package com.example.saastest.modules.auth.dto.response;


public record LoginResponseBody(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}
