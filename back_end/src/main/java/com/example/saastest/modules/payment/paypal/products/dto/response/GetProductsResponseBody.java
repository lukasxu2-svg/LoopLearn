package com.example.saastest.modules.payment.paypal.products.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.example.saastest.modules.payment.paypal.products.dto.ProductDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetProductsResponseBody(
        @JsonProperty("total_items") String totalItems,

        @JsonProperty("total_pages") String totalPages,

        List<LinkDto> links,

        List<ProductDto> products) {
}
