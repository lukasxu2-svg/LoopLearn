package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import com.example.saastest.modules.payment.paypal.orders.dto.enums.CurrencyCodeDto;

public record SetupFeeDto(
        CurrencyCodeDto currencyCode,
        String value) {

}
