package com.example.saastest.modules.auth.dto.response;

import com.example.saastest.modules.benutzer.dto.UserDto;

public record LoginResponseBody(
                UserDto user,
                String accessToken,
                String tokenType,
                Long expiresIn) {
}
