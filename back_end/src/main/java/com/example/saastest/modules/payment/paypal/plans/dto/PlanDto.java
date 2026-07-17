package com.example.saastest.modules.payment.paypal.plans.dto;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.example.saastest.modules.payment.paypal.plans.dto.enums.PlanStatusDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PlanDto(
        String id,
        @JsonProperty("product_id") String productId,
        String name,
        String description,
        PlanStatusDto status,
        @JsonProperty("create_time") String createTime,
        @JsonProperty("update_time") String updateTime,
        List<LinkDto> links) {
}
