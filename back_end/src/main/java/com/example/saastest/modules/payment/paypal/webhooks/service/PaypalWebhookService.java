package com.example.saastest.modules.payment.paypal.webhooks.service;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.CancelSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.service.PayPalSubscriptionService;
import com.example.saastest.modules.payment.paypal.webhooks.dto.WebhookVerificationRequestBody;
import com.example.saastest.modules.payment.service.BaseService;
import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.enums.PlanType;
import com.example.saastest.modules.plan.repository.PlanRepository;
import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.subscription.repository.SubscriptionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


@Service
public class PaypalWebhookService extends BaseService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final PayPalSubscriptionService payPalService;


    public PaypalWebhookService(PaypalTokenService tokenService, WebClient webClient, SubscriptionRepository subscriptionRepository, PlanRepository planRepository, PayPalSubscriptionService payPalService) {
        super(tokenService, webClient);
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.payPalService = payPalService;
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
                String subscriptionId = event.get("resource").get("id").asString();
                Optional<Subscription> activeSubscription = subscriptionRepository.findBySubStatus(SubscriptionStatusDto.ACTIVE);
                Optional<Subscription> pendingSubscription = subscriptionRepository.findBySubStatus(SubscriptionStatusDto.APPROVAL_PENDING);

                //Cancel subscription in db if active plan already exists or pending subscription with different id exists
                if ((activeSubscription.isPresent() && !activeSubscription.get().getPlanType().equals(PlanType.FREE)) ||
                        (pendingSubscription.isPresent() && !pendingSubscription.get().getSubscriptionId().equals(subscriptionId))) {
                    payPalService.paypalCancelSubscription(
                            subscriptionId,
                            new CancelSubscriptionRequestBody("Active/Pending subscription already exists")
                    );
                    throw new IllegalStateException("Active/Pending subscription already exists");
                }

                if (activeSubscription.isPresent() && activeSubscription.get().getPlanType().equals(PlanType.FREE)) {
                    activeSubscription.get().setSubStatus(SubscriptionStatusDto.CANCELLED);
                    subscriptionRepository.save(activeSubscription.get());
                }


                pendingSubscription.get().setSubStatus(SubscriptionStatusDto.ACTIVE);
                subscriptionRepository.save(pendingSubscription.get());
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
                    nextSubscription.setSubStatus(SubscriptionStatusDto.CANCELLED);
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
                    //Creates next subscription if that entry was empty before
                    Instant newPeriodEnd = currentSubscription.getPeriodEnd().atZone(ZoneOffset.UTC).plusMonths(1).toInstant();

                    //Create new subscription entry
                    Subscription newNextSubscription = new Subscription(
                            currentSubscription.getSubscriptionId(),
                            null,
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
                Subscription subscription = subscriptionRepository.findBySubscriptionIdAndSubStatus(subscriptionId, SubscriptionStatusDto.ACTIVE)
                        .orElseThrow(() -> new RuntimeException("Subscription not found"));

                if (subscription.getNextSubscription() != null) {
                    subscription.getNextSubscription().setCanceled();
                    subscription.getNextSubscription().setSubStatus(SubscriptionStatusDto.CANCELLED);
                    subscriptionRepository.save(subscription.getNextSubscription());
                }

                subscription.setCanceled();
                subscription.setNextSubscription(null);
                subscriptionRepository.save(subscription);
                break;
            }

            case "PAYMENT.SALE.COMPLETED": {
                String subscriptionId = event.get("resource").get("billing_agreement_id").asString();
                String captureId = event.get("resource").get("id").asString();

                Optional<Subscription> subscription = subscriptionRepository.findBySubscriptionId(subscriptionId);
                if (subscription.isEmpty()) {
                    payPalService.paypalRefund(captureId);
                }
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
