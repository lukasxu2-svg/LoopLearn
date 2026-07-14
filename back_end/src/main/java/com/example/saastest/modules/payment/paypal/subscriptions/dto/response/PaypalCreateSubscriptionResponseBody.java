package com.example.saastest.modules.payment.paypal.subscriptions.dto.response;

import com.example.saastest.modules.payment.paypal.dto.Link;
import com.example.saastest.modules.payment.paypal.dto.Taxes;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.BillingCycle;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.PaymentPreferences;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaypalCreateSubscriptionResponseBody(
        String id,

        @JsonProperty("product_id")
        String productId,

        String name,

        String description,

        SubscriptionStatus status,

        @JsonProperty("billing_cycles")
        List<BillingCycle> billingCycles,

        @JsonProperty("payment_preferences")
        PaymentPreferences paymentPreferences,

        Taxes taxes,

        @JsonProperty("create_time")
        String createTime,

        @JsonProperty("update_time")
        String updateTime,

        List<Link> links

) {
}
