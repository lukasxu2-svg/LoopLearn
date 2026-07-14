package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import com.example.saastest.modules.payment.paypal.plans.dto.Name;

public record Subscriber(
        String email_address,
        Name name) {
}
