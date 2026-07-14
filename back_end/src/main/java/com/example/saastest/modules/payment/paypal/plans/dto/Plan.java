package com.example.saastest.modules.payment.paypal.plans.dto;

import com.example.saastest.modules.payment.paypal.dto.Link;
import com.example.saastest.modules.payment.paypal.plans.dto.enums.PlanStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Plan(
        String id,
        @JsonProperty("product_id") String productId,
        String name,
        String description,
        PlanStatus status,
        @JsonProperty("create_time") String createTime,
        @JsonProperty("update_time") String updateTime,
        List<Link> links) {
}
