package com.example.saastest.modules.subscription.dto.response;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.enums.PlanType;
import com.example.saastest.modules.subscription.entity.Subscription;

import java.math.BigDecimal;
import java.time.Instant;

public record getCurrentSubscriptionResponseBody(
        SubscriptionStatusDto status,
        PlanType planType,
        BigDecimal cost,
        Instant periodStart,
        Instant periodEnd,
        boolean cancelled,
        getCurrentSubscriptionResponseBody nextSubscription
) {
    public getCurrentSubscriptionResponseBody(Subscription subscription, Plan plan, getCurrentSubscriptionResponseBody nextSubscription) {
        this(
                subscription.getSubStatus(),
                plan.getPlanType(),
                plan.getMonthlyPrice(),
                subscription.getPeriodStart(),
                subscription.getPeriodEnd(),
                subscription.isCanceled(),
                nextSubscription
        );
    }
}
