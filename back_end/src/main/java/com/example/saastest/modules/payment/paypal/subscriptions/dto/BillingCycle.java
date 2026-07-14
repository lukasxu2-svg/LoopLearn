package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.TenureType;

public record BillingCycle(
        Frequency frequency,
        TenureType tenure_type,
        Integer sequence,
        Integer total_cycles) {
}
