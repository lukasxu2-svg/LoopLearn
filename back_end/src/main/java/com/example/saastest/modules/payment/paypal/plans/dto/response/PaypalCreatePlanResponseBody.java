package com.example.saastest.modules.payment.paypal.plans.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.example.saastest.modules.payment.paypal.dto.TaxesDto;
import com.example.saastest.modules.payment.paypal.plans.dto.enums.PlanStatusDto;
import com.example.saastest.modules.payment.paypal.plans.dto.BillingCycleDto;
import com.example.saastest.modules.payment.paypal.plans.dto.PaymentPreferences;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaypalCreatePlanResponseBody(
        String id,

        @JsonProperty("plan_id") String productId,

        String name,

        String description,

        PlanStatusDto status,

        @JsonProperty("billing_cycles") List<BillingCycleDto> billingCycles,

        @JsonProperty("payment_preferences") PaymentPreferences paymentPreferences,

        TaxesDto taxes,

        @JsonProperty("create_time") String createTime,

        @JsonProperty("update_time") String updateTime,

        List<LinkDto> links

) {
}
