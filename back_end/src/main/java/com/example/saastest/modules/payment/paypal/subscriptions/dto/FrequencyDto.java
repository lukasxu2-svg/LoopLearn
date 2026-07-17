package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.IntervalUnitDto;

public record FrequencyDto(
                IntervalUnitDto interval_unit,
                Integer interval_count) {
}
