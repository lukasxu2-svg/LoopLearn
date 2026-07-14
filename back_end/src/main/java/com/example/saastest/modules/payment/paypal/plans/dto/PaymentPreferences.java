package com.example.saastest.modules.payment.paypal.plans.dto;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.SetupFee;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentPreferences(
        @JsonProperty("auto_bill_outstanding") Boolean autoBillOutstanding,
        @JsonProperty("setup_fee") SetupFee setupFee,
        @JsonProperty("setup_fee_failure_action") String setupFeeFailureAction,
        @JsonProperty("payment_failure_threshold") Integer paymentFailureThreshold) {
}
