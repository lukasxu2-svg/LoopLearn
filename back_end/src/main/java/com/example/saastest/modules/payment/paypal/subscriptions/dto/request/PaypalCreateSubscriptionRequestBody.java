package com.example.saastest.modules.payment.paypal.subscriptions.dto.request;

import com.example.saastest.modules.payment.paypal.dto.TaxesDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.BillingCycleDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.PaymentPreferencesDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.SubscriberDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;

import java.util.List;

public record PaypalCreateSubscriptionRequestBody(
        String plan_id, //Not PayPal plan id
        String start_time,
        SubscriberDto subscriber
) {
}
