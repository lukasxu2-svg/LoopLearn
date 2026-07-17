package com.example.saastest.modules.payment.paypal.products.dto.request;

import com.example.saastest.modules.payment.paypal.products.dto.enums.ProductTypesDto;

public record CreateProductRequestBody(
        String id,
        String name,
        ProductTypesDto type) {
}
