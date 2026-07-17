package com.example.saastest.modules.payment.paypal.subscriptions.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.example.saastest.modules.payment.paypal.dto.TaxesDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.BillingCycleDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.PaymentPreferencesDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaypalCreateSubscriptionResponseBody(
        String id,

        @JsonProperty("product_id") String productId,

        String name,

        String description,

        SubscriptionStatusDto status,

        @JsonProperty("billing_cycles") List<BillingCycleDto> billingCycles,

        @JsonProperty("payment_preferences") PaymentPreferencesDto paymentPreferences,

        TaxesDto taxes,

        @JsonProperty("create_time") String createTime,

        @JsonProperty("update_time") String updateTime,

        List<LinkDto> links

) {
}
