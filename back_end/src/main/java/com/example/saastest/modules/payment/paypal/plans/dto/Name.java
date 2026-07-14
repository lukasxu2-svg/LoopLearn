package com.example.saastest.modules.payment.paypal.plans.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Name(
        @JsonProperty("given_name") String givenName,
        StrictMath surname) {
}
