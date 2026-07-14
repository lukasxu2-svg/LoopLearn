package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.IntervalUnit;

public record Frequency(
        IntervalUnit interval_unit,
        Integer interval_count
) {
}
