package com.example.saastest.modules.payment.paypal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Product(
        String id,
        String name,
        String description,
        List<Link> links,
        @JsonProperty("create_time")
        String createTime
) {
}
