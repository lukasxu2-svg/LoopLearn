package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import com.example.saastest.modules.payment.paypal.plans.dto.NameDto;

public record SubscriberDto(
        String email_address,
        NameDto name) {
}
