package com.example.saastest.modules.payment.paypal.subscriptions.controller;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.PaypalCreateSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.PaypalCreateSubscriptionResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.CancelSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.UpdateSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.ListSubscriptionsResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.service.PayPalSubscriptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paypal/subscriptions")
public class PayPalSubscriptionController {

    private final PayPalSubscriptionService service;

    public PayPalSubscriptionController(PayPalSubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public PaypalCreateSubscriptionResponseBody createSubscription(@RequestBody PaypalCreateSubscriptionRequestBody requestBody) {
        return service.createSubscription(requestBody);
    }

    @GetMapping
    public ListSubscriptionsResponseBody listSubscriptions() {
        return service.listSubscriptions();
    }

    @PostMapping("/{subscriptionId}")
    public Object updateSubscription(@PathVariable String subscriptionId, @RequestBody UpdateSubscriptionRequestBody requestBody) {
        return service.updateSubscription(subscriptionId, requestBody);
    }

    @PostMapping("/{subscriptionId}/cancel")
    public Object cancelSubscription(@PathVariable String subscriptionId, @RequestBody CancelSubscriptionRequestBody requestBody) {
        return service.cancelSubscription(subscriptionId, requestBody);
    }
}
