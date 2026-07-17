package com.example.saastest.modules.payment.paypal.plans.dto;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.FrequencyDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.TenureTypeDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BillingCycleDto(
                FrequencyDto frequency,
                TenureTypeDto tenure_type,
                Integer sequence,
                @JsonProperty("total_cycles") Integer totalCycles) {
}
