package com.example.saastest.modules.payment.paypal.plans.dto.response;

import com.example.saastest.modules.payment.paypal.dto.Link;
import com.example.saastest.modules.payment.paypal.dto.Taxes;
import com.example.saastest.modules.payment.paypal.plans.dto.enums.PlanStatus;
import com.example.saastest.modules.payment.paypal.plans.dto.BillingCycle;
import com.example.saastest.modules.payment.paypal.plans.dto.PaymentPreferences;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaypalCreatePlanResponseBody(
                String id,

                @JsonProperty("product_id") String productId,

                String name,

                String description,

                PlanStatus status,

                @JsonProperty("billing_cycles") List<BillingCycle> billingCycles,

                @JsonProperty("payment_preferences") PaymentPreferences paymentPreferences,

                Taxes taxes,

                @JsonProperty("create_time") String createTime,

                @JsonProperty("update_time") String updateTime,

                List<Link> links

) {
}
