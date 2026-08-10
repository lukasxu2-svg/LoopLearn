package com.example.saastest.modules.subscription.dto.request;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.SubscriberDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.plan.enums.PlanType;

import java.math.BigDecimal;

public record CreateSubscriptionRequestBody(
        SubscriberDto subscriber,
        String simplePlanId
) {
}
