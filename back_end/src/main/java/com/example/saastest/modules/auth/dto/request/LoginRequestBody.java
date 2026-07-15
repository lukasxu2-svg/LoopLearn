package com.example.saastest.modules.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestBody(
                @Email String email,
                @NotBlank String password) {
}
