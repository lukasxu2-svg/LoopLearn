package com.example.saastest.modules.payment.paypal.orders.dto.request;

import com.example.saastest.modules.payment.paypal.orders.dto.PurchaseUnit;
import com.example.saastest.modules.payment.paypal.orders.dto.enums.CreateOrderIntent;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateOrderRequestBody(
                CreateOrderIntent intent,
                @JsonProperty("purchase_units") List<PurchaseUnit> purchaseUnits) {
}
