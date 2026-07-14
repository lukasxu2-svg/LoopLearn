package com.example.saastest.modules.payment.paypal.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaypalTokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_in")
        int expiresIn
) {}
