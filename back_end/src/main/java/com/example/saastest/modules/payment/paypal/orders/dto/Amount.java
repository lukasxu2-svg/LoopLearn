package com.example.saastest.modules.payment.paypal.orders.dto;

import com.example.saastest.modules.payment.paypal.orders.dto.enums.CurrencyCode;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Amount(
                @JsonProperty("currency_code") CurrencyCode currencyCode,
                String value) {
}
