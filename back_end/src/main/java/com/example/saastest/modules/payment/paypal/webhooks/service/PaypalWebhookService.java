package com.example.saastest.modules.payment.paypal.webhooks.service;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.subscription.repository.SubscriptionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


@Service
public class PaypalWebhookService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SubscriptionRepository subscriptionRepository;

    public PaypalWebhookService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void receiveWebhook(String body, HttpServletRequest request) {
        JsonNode event = objectMapper.readTree(body);

        String eventType = event.get("event_type").asString();
        System.out.println(event);

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
}
