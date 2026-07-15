package com.example.saastest.modules.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseBody(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}
