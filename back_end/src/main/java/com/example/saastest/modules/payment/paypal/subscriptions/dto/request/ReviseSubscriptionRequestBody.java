package com.example.saastest.modules.payment.paypal.subscriptions.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReviseSubscriptionRequestBody(
        @JsonProperty("plan_id")
        String planId
) {
}
