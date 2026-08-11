package com.example.saastest.modules.payment.paypal.webhooks.controller;

import com.example.saastest.modules.payment.paypal.webhooks.dto.WebhookVerificationRequestBody;
import com.example.saastest.modules.payment.paypal.webhooks.service.PaypalWebhookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/api/paypal/webhooks")
public class PaypalWebhookController {

    @Value("${paypal.webhook.id}")
    private String webhookId;
    private final PaypalWebhookService service;

    public PaypalWebhookController(PaypalWebhookService service) {
        this.service = service;
    }

    /*@PostMapping
    public void receiveWebhook(@RequestBody String body, HttpServletRequest request) {
        service.receiveWebhook(body, request);
    }*/

    @PostMapping
    public void receiveWebhook(@RequestHeader("PAYPAL-AUTH-ALGO") String authAlgo,
                               @RequestHeader("PAYPAL-CERT-URL") String certUrl,
                               @RequestHeader("PAYPAL-TRANSMISSION-ID") String transmissionId,
                               @RequestHeader("PAYPAL-TRANSMISSION-TIME") String transmissionTime,
                               @RequestHeader("PAYPAL-TRANSMISSION-SIG") String transmissionSig,
                               @RequestBody JsonNode webhookEvent) {
        WebhookVerificationRequestBody requestBody = new WebhookVerificationRequestBody(
                transmissionId,
                transmissionTime,
                transmissionSig,
                certUrl,
                authAlgo,
                webhookId,
                webhookEvent
        );
        
        service.receiveWebhook(webhookEvent, requestBody);
    }
}
