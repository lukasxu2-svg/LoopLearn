package com.example.saastest.modules.payment.paypal.orders.dto.request;

import com.example.saastest.modules.payment.paypal.orders.dto.PurchaseUnitDto;
import com.example.saastest.modules.payment.paypal.orders.dto.enums.CreateOrderIntentDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateOrderRequestBody(
        CreateOrderIntentDto intent,
        @JsonProperty("purchase_units") List<PurchaseUnitDto> purchaseUnits) {
}
