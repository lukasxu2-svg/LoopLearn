package com.example.saastest.modules.payment.paypal.webhooks.service;

import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.payment.paypal.webhooks.dto.WebhookVerificationRequestBody;
import com.example.saastest.modules.payment.service.BaseService;
import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.subscription.repository.SubscriptionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


@Service
public class PaypalWebhookService extends BaseService {
    private final SubscriptionRepository subscriptionRepository;

    public PaypalWebhookService(PaypalTokenService tokenService, WebClient webClient, SubscriptionRepository subscriptionRepository) {
        super(tokenService, webClient);
        this.subscriptionRepository = subscriptionRepository;
    }

    public void receiveWebhook(JsonNode webEvent, WebhookVerificationRequestBody requestBody) {
        JsonNode response = verify(requestBody);

        if (!response.get("verification_status").asString().equals("SUCCESS")) {
            return;
        }
        JsonNode event = webEvent;

        String eventType = event.get("event_type").asString();

        switch (eventType) {

            case "BILLING.SUBSCRIPTION.ACTIVATED": {
                //Check if old subscription is active
                String subscriptionId = event.get("resource").get("id").asString();
                setSubscriptionStatus(subscriptionId, SubscriptionStatusDto.ACTIVE);
                break;
            }

            case "BILLING.SUBSCRIPTION.CANCELLED": {
                String subscriptionId = event.get("resource").get("id").asString();
                setSubscriptionStatus(subscriptionId, SubscriptionStatusDto.CANCELLED);
                break;
            }

            default:
                break;
        }
    }

    private void setSubscriptionStatus(String id, SubscriptionStatusDto status) {
        Subscription subscription = subscriptionRepository.findBySubscriptionId(id).orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription.setSubStatus(status);
        subscriptionRepository.save(subscription);
    }

    private JsonNode verify(WebhookVerificationRequestBody requestBody) {
        String uri = "https://api.sandbox.paypal.com/v1/notifications/verify-webhook-signature";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        return post(uri, headers, requestBody, JsonNode.class);
    }
}
