package com.example.saastest.modules.subscription.controller;

import com.example.saastest.modules.subscription.dto.request.CreateSubscriptionRequestBody;
import com.example.saastest.modules.subscription.entity.Subscription;
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
    public Subscription getCurrentSubscription(Authentication authentication) {
        Long benutzerId = Long.valueOf(authentication.getName());

        return service.getCurrentSubscription(benutzerId);
    }

    @DeleteMapping("/${subscriptionId}/cancel")
    public void deleteCurrentSubscription(@PathVariable Long subId) {
        service.deleteCurrentSubscription(subId);
    }

    @PostMapping
    public Subscription createSubscription(@RequestBody CreateSubscriptionRequestBody body) {
        return service.createSubscription(body);
    }
}
