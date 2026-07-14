package com.example.saastest.modules.payment.paypal.products.dto;

import com.example.saastest.modules.payment.paypal.dto.Link;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Product(
        String id,
        String name,
        String description,
        List<Link> links,
        @JsonProperty("create_time") String createTime) {
}
