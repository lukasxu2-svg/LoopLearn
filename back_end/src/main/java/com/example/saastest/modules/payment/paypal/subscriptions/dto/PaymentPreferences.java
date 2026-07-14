package com.example.saastest.modules.payment.paypal.subscriptions.dto;

public record PaymentPreferences(
        Boolean auto_bill_outstanding,
        String setup_fee_failure_action,
        Integer payment_failure_threshold
) {
}
