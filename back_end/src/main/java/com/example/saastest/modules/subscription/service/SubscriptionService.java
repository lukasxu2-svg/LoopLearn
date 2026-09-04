package com.example.saastest.modules.subscription.service;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import com.example.saastest.modules.payment.paypal.plans.dto.NameDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.SubscriberDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.CancelSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.PaypalCreateSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.ReviseSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.GetSubscriptionByIdResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.PaypalCreateSubscriptionResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.ReviseSubscriptionResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.service.PayPalSubscriptionService;
import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.enums.PlanType;
import com.example.saastest.modules.plan.repository.PlanRepository;
import com.example.saastest.modules.subscription.dto.request.CreateSubscriptionRequestBody;
import com.example.saastest.modules.subscription.dto.response.CreateSubscriptionResponseBody;
import com.example.saastest.modules.subscription.dto.response.getCurrentSubscriptionResponseBody;
import com.example.saastest.modules.subscription.entity.Subscription;
import com.example.saastest.modules.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

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
        Optional<Subscription> subscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzerId, SubscriptionStatusDto.ACTIVE);

        if (subscription.isEmpty()) {
            return null;
        }

        //Check if current subscription is still valid
        //If not create new free subscription
        if (!subscription.get().getPlanType().equals(PlanType.FREE) && subscription.get().isCanceled() && Instant.now().isAfter(subscription.get().getPeriodEnd())) {
            subscription.get().setSubStatus(SubscriptionStatusDto.CANCELLED);
            subscriptionRepository.save(subscription.get());

            Benutzer benutzer = benutzerRepository.findById(benutzerId).orElseThrow(() -> new RuntimeException("User not found"));
            Subscription newSubscription = new Subscription(benutzer, PlanType.FREE, SubscriptionStatusDto.ACTIVE);
            subscriptionRepository.save(newSubscription);
            return null;
        }

        Plan plan = planRepository.findByPlanType(subscription.get().getPlanType()).orElseThrow(() -> new RuntimeException("Plan not found"));

        getCurrentSubscriptionResponseBody nextSubscription = null;
        if (subscription.get().getNextSubscription() != null) {
            Plan nextPlan = planRepository.findByPlanType(subscription.get().getNextSubscription().getPlanType()).orElseThrow(() -> new RuntimeException("Plan not found"));
            nextSubscription = new getCurrentSubscriptionResponseBody(subscription.get().getNextSubscription(), nextPlan, null);
        }

        getCurrentSubscriptionResponseBody responseBody = new getCurrentSubscriptionResponseBody(subscription.get(), plan, nextSubscription);

        return responseBody;
    }

    public void cancelCurrentSubscription(Long benutzerId) {
        Subscription subscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzerId, SubscriptionStatusDto.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found"));

        if (!subscription.getPlanType().equals(PlanType.FREE)) {
            String subId = subscription.getSubscriptionId();
            CancelSubscriptionRequestBody requestBody = new CancelSubscriptionRequestBody("Cancel");
            payPalService.paypalCancelSubscription(subId, requestBody);
        } else {
            subscription.setCancelAll();
            subscriptionRepository.save(subscription);
        }
    }

    public Object cancelNextSubscription(Long benutzerId) {
        Subscription currentSubscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzerId, SubscriptionStatusDto.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found"));

        //Revise because current subscription plan type was changed
        //This simply reverts it
        return payPalService.paypalReviseSubscription(currentSubscription.getSubscriptionId(), new ReviseSubscriptionRequestBody(currentSubscription.getPlanId()));
    }


    @Transactional
    public void createFreeSubscription(CreateSubscriptionRequestBody requestBody) {
        Benutzer benutzer = benutzerRepository.findByEmail(requestBody.subscriber().email_address()).orElseThrow(() -> new RuntimeException("User not found"));

        Subscription subscription = new Subscription(benutzer, PlanType.FREE, SubscriptionStatusDto.ACTIVE);
        subscriptionRepository.save(subscription);
    }


    @Transactional
    public Object createSubscription(CreateSubscriptionRequestBody requestBody) {
        Plan plan = planRepository.findById(Long.parseLong(requestBody.simplePlanId())).orElseThrow(() -> new RuntimeException("Plan not found"));
        String planId = plan.getPlanId();

        //Lock to one instance
        Benutzer benutzer = benutzerRepository.findByEmail(requestBody.subscriber().email_address()).orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Subscription> activeSubscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzer.getId(), SubscriptionStatusDto.ACTIVE);
        Optional<Subscription> pendingSubscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzer.getId(), SubscriptionStatusDto.APPROVAL_PENDING);

        if ((activeSubscription.isPresent() && !activeSubscription.get().getPlanType().equals(PlanType.FREE))) {
            throw new IllegalStateException("Pending Subscription already exists");
        }

        if (pendingSubscription.isPresent() && !plan.getPlanType().equals(pendingSubscription.get().getPlanType())) {
            throw new IllegalStateException("Pending subscription of type " + pendingSubscription.get().getPlanType() + " already exists");
        }


        if (pendingSubscription.isPresent() && plan.getPlanType().equals(pendingSubscription.get().getPlanType())) {
            return restartPendingSubscription(pendingSubscription.get());
        }

        if (plan.getPlanType().equals(PlanType.FREE)) {
            Subscription subscription = new Subscription(benutzer, PlanType.FREE, SubscriptionStatusDto.ACTIVE);
            subscriptionRepository.save(subscription);
            return null;
        }

        //Create Paypal subscription if no subscription exists or plan type is free
        String requestId = pendingSubscription.isEmpty() ? UUID.randomUUID().toString() : pendingSubscription.get().getPaypalRequestId();

        PaypalCreateSubscriptionRequestBody paypalBody = new PaypalCreateSubscriptionRequestBody(planId, requestBody.subscriber());
        PaypalCreateSubscriptionResponseBody paypalResponse = payPalService.paypalCreateSubscription(paypalBody, requestId);

        System.out.println("------------" + paypalResponse + "-----------------");

        if (paypalResponse.status() != SubscriptionStatusDto.APPROVAL_PENDING) {
            throw new RuntimeException("PayPal could not create a subscription");
        }

        //Create Subscription entity
        Instant periodStart = OffsetDateTime.parse(paypalResponse.createTime()).toInstant();
        Instant periodEnd = OffsetDateTime.parse(paypalResponse.createTime()).plusMonths(1).toInstant();

        Subscription subscription = new Subscription(
                paypalResponse.id(),
                requestId,
                benutzer,
                planId,
                plan.getPlanType(),
                SubscriptionStatusDto.APPROVAL_PENDING,
                periodStart,
                periodEnd);

        subscriptionRepository.save(subscription);

        CreateSubscriptionResponseBody response = new CreateSubscriptionResponseBody(paypalResponse.links());
        return response;
    }

    private Object restartPendingSubscription(Subscription pendingSubscription) {
        GetSubscriptionByIdResponseBody response = null;
        try {
            response = payPalService.getSubscriptionById(pendingSubscription.getSubscriptionId());
        } catch (WebClientResponseException e) {
            //Set pending subscription to canceled only if paypal could not find it
            if (e.getStatusCode().value() != 404) {
                throw new IllegalStateException("Getting subscription by id failed");
            }
            pendingSubscription.setCancelAll();
            subscriptionRepository.save(pendingSubscription);
        }

        if (response == null) {
            throw new IllegalStateException("PayPal response was null");
        }
        
        switch (response.status()) {
            case SubscriptionStatusDto.ACTIVE: {
                pendingSubscription.setSubStatus(SubscriptionStatusDto.ACTIVE);
                subscriptionRepository.save(pendingSubscription);
                break;
            }
            case SubscriptionStatusDto.APPROVAL_PENDING: {
                return new CreateSubscriptionResponseBody(response.links());
            }
            case SubscriptionStatusDto.CANCELLED: {
                pendingSubscription.setCancelAll();
                subscriptionRepository.save(pendingSubscription);
                break;
            }
            case SubscriptionStatusDto.EXPIRED: {
                pendingSubscription.setCancelAll();
                subscriptionRepository.save(pendingSubscription);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected subscription state");
            }
        }
        return null;
    }

    @Transactional
    public ReviseSubscriptionResponseBody reviseSubscription(CreateSubscriptionRequestBody body) {
        Benutzer benutzer = benutzerRepository.findByEmail(body.subscriber().email_address()).orElseThrow(() -> new RuntimeException("User not found"));
        Subscription activeSubscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzer.getId(), SubscriptionStatusDto.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found"));

        Plan currentPlan = planRepository.findByPlanType(activeSubscription.getPlanType()).orElseThrow(() -> new RuntimeException("Plan not found"));
        Plan plan = planRepository.findById(Long.parseLong(body.simplePlanId())).orElseThrow(() -> new RuntimeException("Plan not found"));

        //Throw error if user tries to change the plan of incoming subscription to the same as current one
        if (plan.getRank().equals(currentPlan.getRank())) {
            throw new IllegalStateException("The rank of the new subscription is the same as the old one");
        }

        if (activeSubscription.getNextSubscription() != null && activeSubscription.getNextSubscription().getPlanType().equals(plan.getPlanType())) {
            throw new IllegalStateException("The rank of the new subscription is the same as the incoming one");
        }

        ReviseSubscriptionRequestBody requestBody = new ReviseSubscriptionRequestBody(plan.getPlanId());

        return payPalService.paypalReviseSubscription(activeSubscription.getSubscriptionId(), requestBody);
    }
}
