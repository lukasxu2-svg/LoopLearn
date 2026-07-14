package com.example.saastest.modules.payment.paypal.plans.dto;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.Frequency;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.TenureType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BillingCycle(
        Frequency frequency,
        TenureType tenure_type,
        Integer sequence,
        @JsonProperty("total_cycles") Integer totalCycles) {
}
