package com.example.saastest.modules.payment.paypal.webhooks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.JsonNode;

public record WebhookVerificationRequestBody(
        @JsonProperty("transmission_id")
        String transmissionId,
        @JsonProperty("transmission_time")
        String transmissionTime,
        @JsonProperty("transmission_sig")
        String transmissionSig,
        @JsonProperty("cert_url")
        String certUrl,
        @JsonProperty("auth_algo")
        String authAlgo,
        @JsonProperty("webhook_id")
        String webhookId,
        @JsonProperty("webhook_event")
        JsonNode event
) {
}
