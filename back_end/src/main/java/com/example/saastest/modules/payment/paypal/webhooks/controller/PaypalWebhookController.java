package com.example.saastest.modules.payment.paypal.webhooks.controller;

import com.example.saastest.modules.payment.paypal.webhooks.service.PaypalWebhookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/api/paypal/webhooks")
public class PaypalWebhookController {

    private final PaypalWebhookService service;

    public PaypalWebhookController(PaypalWebhookService service) {
        this.service = service;
    }

    @PostMapping
    public void receiveWebhook(@RequestBody String body, HttpServletRequest request) {
        service.receiveWebhook(body, request);
    }
}
