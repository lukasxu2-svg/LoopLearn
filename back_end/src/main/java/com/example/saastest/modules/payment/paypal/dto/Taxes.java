package com.example.saastest.modules.payment.paypal.dto;

public record Taxes(
        String percentage,
        Boolean inclusive
) {
}
