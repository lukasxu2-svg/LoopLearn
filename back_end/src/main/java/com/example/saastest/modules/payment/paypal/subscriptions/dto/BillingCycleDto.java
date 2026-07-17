package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.TenureTypeDto;

public record BillingCycleDto(
                FrequencyDto frequency,
                TenureTypeDto tenure_type,
                Integer sequence,
                Integer total_cycles) {
}
