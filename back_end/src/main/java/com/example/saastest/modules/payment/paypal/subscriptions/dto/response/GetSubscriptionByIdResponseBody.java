package com.example.saastest.modules.payment.paypal.subscriptions.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.BillingCycleDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.PaymentPreferencesDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GetSubscriptionByIdResponseBody(
        String id,
        String plan_id,
        String name,
        SubscriptionStatusDto status,
        String description,

        @JsonProperty("billing_cycles")
        BillingCycleDto billingCycles,

        @JsonProperty("payment_preferences")
        PaymentPreferencesDto paymentPreferences,

        String create_time,

        LinkDto links
) {
}
