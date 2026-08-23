package com.example.saastest.modules.subscription.controller;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.ReviseSubscriptionRequestBody;
import com.example.saastest.modules.subscription.dto.request.CreateSubscriptionRequestBody;
import com.example.saastest.modules.subscription.dto.response.CreateSubscriptionResponseBody;
import com.example.saastest.modules.subscription.dto.response.getCurrentSubscriptionResponseBody;
import com.example.saastest.modules.subscription.service.SubscriptionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/subscription")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping("/current")
    public getCurrentSubscriptionResponseBody getCurrentSubscription(Authentication authentication) {
        Long benutzerId = Long.valueOf(authentication.getName());

        return service.getCurrentSubscription(benutzerId);
    }

    @PostMapping("/current/cancel")
    public void cancelCurrentSubscription(Authentication authentication) {
        Long benutzerId = Long.valueOf(authentication.getName());

        service.cancelCurrentSubscription(benutzerId);
    }

    @PostMapping("/next/cancel")
    public Object cancelNextSubscription(Authentication authentication) {
        Long benutzerId = Long.valueOf(authentication.getName());

        return service.cancelNextSubscription(benutzerId);
    }

    @PostMapping
    public Object createSubscription(@RequestBody CreateSubscriptionRequestBody body) {
        return service.createSubscription(body);
    }

    @PostMapping("/free")
    public void createFreeSubscription(@RequestBody CreateSubscriptionRequestBody body) {
        service.createFreeSubscription(body);
    }

    @PostMapping("/revise")
    public void reviseSubscription(@RequestBody CreateSubscriptionRequestBody body) {
        service.reviseSubscription(body);
    }
}
