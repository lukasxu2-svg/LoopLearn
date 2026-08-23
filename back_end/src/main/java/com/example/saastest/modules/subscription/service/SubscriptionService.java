package com.example.saastest.modules.subscription.service;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import com.example.saastest.modules.payment.paypal.plans.dto.NameDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.SubscriberDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.CancelSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.PaypalCreateSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.ReviseSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.PaypalCreateSubscriptionResponseBody;
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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

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

        if (subscription.getPlanType().equals(PlanType.FREE)) {
            subscription.setSubStatus(SubscriptionStatusDto.CANCELLED);
            subscription.setCanceled();
            subscriptionRepository.save(subscription);
        } else {
            String subId = subscription.getSubscriptionId();
            CancelSubscriptionRequestBody requestBody = new CancelSubscriptionRequestBody("Cancel");
            payPalService.paypalCancelSubscription(subId, requestBody);
        }
    }

    public Object cancelNextSubscription(Long benutzerId) {
        Subscription currentSubscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzerId, SubscriptionStatusDto.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found"));

        return payPalService.paypalReviseSubscription(currentSubscription.getSubscriptionId(), new ReviseSubscriptionRequestBody(currentSubscription.getPlanId()));
    }


    public void createFreeSubscription(CreateSubscriptionRequestBody requestBody) {
        Benutzer benutzer = benutzerRepository.findByEmail(requestBody.subscriber().email_address()).orElseThrow(() -> new RuntimeException("User not found"));

        Subscription subscription = new Subscription(benutzer, PlanType.FREE, SubscriptionStatusDto.ACTIVE);
        subscriptionRepository.save(subscription);
    }


    public Object createSubscription(CreateSubscriptionRequestBody requestBody) {
        Plan plan = planRepository.findById(Long.parseLong(requestBody.simplePlanId())).orElseThrow(() -> new RuntimeException("Plan not found"));
        String planId = plan.getPlanId();

        Benutzer benutzer = benutzerRepository.findByEmail(requestBody.subscriber().email_address()).orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Subscription> currentSubscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzer.getId(), SubscriptionStatusDto.ACTIVE);

        //Create Paypal subscription if no subscription exists or plan type is free
        if (currentSubscription.isEmpty() || currentSubscription.get().getPlanType().equals(PlanType.FREE)) {
            PaypalCreateSubscriptionRequestBody paypalBody = new PaypalCreateSubscriptionRequestBody(planId, requestBody.subscriber());
            PaypalCreateSubscriptionResponseBody paypalResponse = payPalService.paypalCreateSubscription(paypalBody);

            //Create Subscription entity
            if (paypalResponse.status() == SubscriptionStatusDto.APPROVAL_PENDING) {
                Instant periodStart = OffsetDateTime.parse(paypalResponse.createTime()).toInstant();
                Instant periodEnd = OffsetDateTime.parse(paypalResponse.createTime()).plusMonths(1).toInstant();

                Subscription subscription = new Subscription(paypalResponse.id(), benutzer, planId, plan.getPlanType(), SubscriptionStatusDto.APPROVAL_PENDING, periodStart, periodEnd);

                subscriptionRepository.save(subscription);
            }
            CreateSubscriptionResponseBody response = new CreateSubscriptionResponseBody(paypalResponse.links());
            return response;
        } else {
            Plan currentPlan = planRepository.findByPlanType(currentSubscription.get().getPlanType()).orElseThrow(() -> new RuntimeException("Plan not found"));

            //Do nothing if the to created plan has same rank as current one
            if (plan.getRank().equals(currentPlan.getRank())) {
                return null;
            }
            ReviseSubscriptionRequestBody body = new ReviseSubscriptionRequestBody(plan.getPlanId());

            return payPalService.paypalReviseSubscription(currentSubscription.get().getSubscriptionId(), body);
        }
    }

    public void reviseSubscription(CreateSubscriptionRequestBody body) {
        Plan plan = planRepository.findById(Long.parseLong(body.simplePlanId())).orElseThrow(() -> new RuntimeException("Plan not found"));

        Benutzer benutzer = benutzerRepository.findByEmail(body.subscriber().email_address()).orElseThrow(() -> new RuntimeException("User not found"));

        Subscription oldSubscription = subscriptionRepository.findByBenutzer_IdAndSubStatus(benutzer.getId(), SubscriptionStatusDto.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found"));


        ReviseSubscriptionRequestBody requestBody = new ReviseSubscriptionRequestBody(plan.getPlanId());
        payPalService.paypalReviseSubscription(oldSubscription.getSubscriptionId(), requestBody);
    }
}
