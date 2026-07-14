package com.example.saastest.modules.payment.paypal.products.dto.request;

import com.example.saastest.modules.payment.paypal.products.dto.enums.ProductTypes;

public record CreateProductRequestBody(
                String id,
                String name,
                ProductTypes type) {
}
