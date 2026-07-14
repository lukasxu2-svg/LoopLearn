package com.example.saastest.modules.payment.paypal.subscriptions.dto;


import com.example.saastest.modules.payment.paypal.orders.dto.enums.CurrencyCode;

public record SetupFee(
        CurrencyCode currencyCode,
        String value
) {

}
