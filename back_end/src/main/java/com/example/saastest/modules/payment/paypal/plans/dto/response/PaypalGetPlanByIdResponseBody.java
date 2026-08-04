package com.example.saastest.modules.payment.paypal.plans.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.example.saastest.modules.payment.paypal.plans.dto.enums.PlanStatusDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaypalGetPlanByIdResponseBody(
        @JsonProperty("id")
        String planId,
        String name,
        PlanStatusDto status,
        @JsonProperty("create_time")
        String createTime,
        List<LinkDto> links
) {
}
