package com.example.saastest.modules.payment.paypal.subscriptions.dto;

public record PaymentPreferencesDto(
                Boolean auto_bill_outstanding,
                String setup_fee_failure_action,
                Integer payment_failure_threshold) {
}
