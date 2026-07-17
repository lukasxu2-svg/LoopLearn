package com.example.saastest.modules.payment.paypal.orders.dto;

import com.example.saastest.modules.payment.paypal.orders.dto.enums.CurrencyCodeDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AmountDto(
                @JsonProperty("currency_code") CurrencyCodeDto currencyCode,
                String value) {
}
