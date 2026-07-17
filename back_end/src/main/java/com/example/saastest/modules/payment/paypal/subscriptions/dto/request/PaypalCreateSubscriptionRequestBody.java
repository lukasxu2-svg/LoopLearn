package com.example.saastest.modules.payment.paypal.subscriptions.dto.request;

import com.example.saastest.modules.payment.paypal.dto.TaxesDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.BillingCycleDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.PaymentPreferencesDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;

import java.util.List;

public record PaypalCreateSubscriptionRequestBody(
        String product_id,
        String name,
        String description,
        SubscriptionStatusDto status,
        List<BillingCycleDto> billing_cycles,
        PaymentPreferencesDto payment_preferences,
        TaxesDto taxes) {
}
