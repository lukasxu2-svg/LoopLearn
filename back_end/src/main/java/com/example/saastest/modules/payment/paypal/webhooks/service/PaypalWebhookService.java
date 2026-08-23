package com.example.saastest.modules.payment.paypal.webhooks.service;

import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.payment.paypal.webhooks.dto.WebhookVerificationRequestBody;
import com.example.saastest.modules.payment.service.BaseService;
import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.repository.PlanRepository;
import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.subscription.repository.SubscriptionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;


@Service
public class PaypalWebhookService extends BaseService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    public PaypalWebhookService(PaypalTokenService tokenService, WebClient webClient, SubscriptionRepository subscriptionRepository, PlanRepository planRepository) {
        super(tokenService, webClient);
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
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
                Subscription subscription = subscriptionRepository.findBySubscriptionId(subscriptionId).orElseThrow(() -> new RuntimeException("Subscription not found"));

                Optional<Subscription> activeSub = subscriptionRepository.findBySubStatus(SubscriptionStatusDto.ACTIVE);

                if (activeSub.isPresent()) {
                    activeSub.get().setSubStatus(SubscriptionStatusDto.CANCELLED);
                    subscriptionRepository.save(activeSub.get());
                }

                subscription.setSubStatus(SubscriptionStatusDto.ACTIVE);
                subscriptionRepository.save(subscription);
                break;
            }

            case "BILLING.SUBSCRIPTION.UPDATED": {
                //Check if current subscription was updated
                String subscriptionId = event.get("resource").get("id").asString();
                String planId = event.get("resource").get("plan_id").asString();

                Subscription currentSubscription = subscriptionRepository.findBySubscriptionIdAndSubStatus(subscriptionId, SubscriptionStatusDto.ACTIVE)
                        .orElseThrow(() -> new RuntimeException("Subscription not found"));
                Subscription nextSubscription = currentSubscription.getNextSubscription();
                Plan newPlan = planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("Subscription not found"));

                //If updated subscription has same plan type as current, cancel next subscription
                if (newPlan.getPlanType().equals(currentSubscription.getPlanType())) {
                    nextSubscription.setCanceled();
                    subscriptionRepository.save(nextSubscription);

                    currentSubscription.setNextSubscription(null);
                    subscriptionRepository.save(currentSubscription);
                    break;
                }

                //If updated subscription has other plan type than current one and next subscription exists,
                //adjust next subscription entry
                if (nextSubscription != null) {
                    nextSubscription.setPlanId(planId);
                    nextSubscription.setPlanType(newPlan.getPlanType());
                    subscriptionRepository.save(nextSubscription);
                } else {
                    Instant newPeriodEnd = currentSubscription.getPeriodEnd().atZone(ZoneOffset.UTC).plusMonths(1).toInstant();

                    //Create new subscription entry
                    Subscription newNextSubscription = new Subscription(
                            currentSubscription.getSubscriptionId(),
                            currentSubscription.getBenutzer(),
                            planId,
                            newPlan.getPlanType(),
                            SubscriptionStatusDto.APPROVED,
                            currentSubscription.getPeriodEnd(),
                            newPeriodEnd);

                    subscriptionRepository.save(newNextSubscription);

                    //Adjust next subscription
                    currentSubscription.setNextSubscription(newNextSubscription);
                    subscriptionRepository.save(currentSubscription);
                }
                break;
            }

            case "BILLING.SUBSCRIPTION.CANCELLED": {
                String subscriptionId = event.get("resource").get("id").asString();
                Subscription subscription = subscriptionRepository.findBySubscriptionId(subscriptionId).orElseThrow(() -> new RuntimeException("Subscription not found"));
                subscription.setCanceled();
                subscriptionRepository.save(subscription);
                break;
            }

            default:
                break;
        }
    }

    private JsonNode verify(WebhookVerificationRequestBody requestBody) {
        String uri = "https://api.sandbox.paypal.com/v1/notifications/verify-webhook-signature";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        return post(uri, headers, requestBody, JsonNode.class);
    }
}
