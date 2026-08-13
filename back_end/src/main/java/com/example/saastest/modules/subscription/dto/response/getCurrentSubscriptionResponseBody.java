package com.example.saastest.modules.subscription.dto.response;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.plan.enums.PlanType;

import java.math.BigDecimal;
import java.time.Instant;

public record getCurrentSubscriptionResponseBody(
        SubscriptionStatusDto status,
        PlanType planType,
        BigDecimal cost,
        Instant periodStart,
        Instant periodEnd,
        boolean cancelled
) {
}
