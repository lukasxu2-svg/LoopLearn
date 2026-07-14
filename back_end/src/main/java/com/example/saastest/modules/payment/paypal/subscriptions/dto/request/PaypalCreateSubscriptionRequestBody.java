package com.example.saastest.modules.payment.paypal.subscriptions.dto.request;

import com.example.saastest.modules.payment.paypal.dto.Taxes;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.BillingCycle;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.PaymentPreferences;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatus;

import java.util.List;

public record PaypalCreateSubscriptionRequestBody(
        String product_id,
        String name,
        String description,
        SubscriptionStatus status,
        List<BillingCycle> billing_cycles,
        PaymentPreferences payment_preferences,
        Taxes taxes
) {
}
