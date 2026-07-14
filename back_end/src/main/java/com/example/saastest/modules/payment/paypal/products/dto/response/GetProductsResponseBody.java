package com.example.saastest.modules.payment.paypal.products.dto.response;

import com.example.saastest.modules.payment.paypal.dto.Link;
import com.example.saastest.modules.payment.paypal.products.dto.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetProductsResponseBody(
                @JsonProperty("total_items") String totalItems,

                @JsonProperty("total_pages") String totalPages,

                List<Link> links,

                List<Product> products) {
}
