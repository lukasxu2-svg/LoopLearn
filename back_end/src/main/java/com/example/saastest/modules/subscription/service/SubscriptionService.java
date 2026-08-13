package com.example.saastest.modules.subscription.service;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.CancelSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.PaypalCreateSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.PaypalCreateSubscriptionResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.service.PayPalSubscriptionService;
import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.repository.PlanRepository;
import com.example.saastest.modules.subscription.dto.request.CreateSubscriptionRequestBody;
import com.example.saastest.modules.subscription.dto.response.CreateSubscriptionResponseBody;
import com.example.saastest.modules.subscription.dto.response.getCurrentSubscriptionResponseBody;
import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PayPalSubscriptionService payPalService;
    private final PlanRepository planRepository;
    private final BenutzerRepository benutzerRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, PayPalSubscriptionService payPalService, PlanRepository planRepository, BenutzerRepository benutzerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.payPalService = payPalService;
        this.planRepository = planRepository;
        this.benutzerRepository = benutzerRepository;
    }

    public getCurrentSubscriptionResponseBody getCurrentSubscription(Long benutzerId) {
        Subscription subscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzerId, SubscriptionStatusDto.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found"));
        Plan plan = planRepository.findByPlanId(subscription.getPlanId()).orElseThrow(() -> new RuntimeException("Plan not found"));

        if (subscription.isCanceled() && Instant.now().isAfter(subscription.getPeriodEnd())) {
            subscription.setSubStatus(SubscriptionStatusDto.CANCELLED);
            return null;
        }

        getCurrentSubscriptionResponseBody responseBody = new getCurrentSubscriptionResponseBody(
                subscription.getSubStatus(),
                plan.getPlanType(),
                plan.getMonthlyPrice(),
                subscription.getPeriodStart(),
                subscription.getPeriodEnd(),
                subscription.isCanceled()
        );

        return responseBody;
    }

    public void cancelCurrentSubscription(Long benutzerId) {
        Subscription subscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzerId, SubscriptionStatusDto.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found"));

        String subId = subscription.getSubscriptionId();
        CancelSubscriptionRequestBody requestBody = new CancelSubscriptionRequestBody("Cancel");
        payPalService.cancelSubscription(subId, requestBody);
    }


    public CreateSubscriptionResponseBody createSubscription(CreateSubscriptionRequestBody requestBody) {
        Plan plan = planRepository.findById(Long.parseLong(requestBody.simplePlanId())).orElseThrow(() -> new RuntimeException("Plan not found"));
        String planId = plan.getPlanId();

        //Create Paypal Subscription
        PaypalCreateSubscriptionRequestBody paypalBody = new PaypalCreateSubscriptionRequestBody(planId, requestBody.subscriber());
        PaypalCreateSubscriptionResponseBody paypalResponse = payPalService.createSubscription(paypalBody);

        //Create Subscription entity
        if (paypalResponse.status() == SubscriptionStatusDto.APPROVAL_PENDING) {
            Benutzer benutzer = benutzerRepository.findByEmail(requestBody.subscriber().email_address()).orElseThrow(() -> new RuntimeException("User not found"));
            Instant periodStart = OffsetDateTime.parse(paypalResponse.createTime()).toInstant();
            Instant periodEnd = OffsetDateTime.parse(paypalResponse.createTime()).plusMonths(1).toInstant();

            Subscription subscription = new Subscription(paypalResponse.id(), benutzer, planId, SubscriptionStatusDto.APPROVAL_PENDING, periodStart, periodEnd);

            subscriptionRepository.save(subscription);
        }
        CreateSubscriptionResponseBody response = new CreateSubscriptionResponseBody(paypalResponse.links());
        return response;
    }
}
