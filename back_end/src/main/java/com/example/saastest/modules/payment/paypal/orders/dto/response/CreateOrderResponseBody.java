package com.example.saastest.modules.payment.paypal.orders.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;

import java.util.List;

public record CreateOrderResponseBody(
        String id,
        String status,
        List<LinkDto> links) {
}
