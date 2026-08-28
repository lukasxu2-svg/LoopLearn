package com.example.saastest.modules.payment.paypal.subscriptions.service;

import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.PaypalCreateSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.ReviseSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.GetSubscriptionByIdResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.PaypalCreateSubscriptionResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.CancelSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.request.UpdateSubscriptionRequestBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.ListSubscriptionsResponseBody;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.response.ReviseSubscriptionResponseBody;
import com.example.saastest.modules.payment.service.BaseService;
import com.example.saastest.modules.plan.repository.PlanRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.UUID;

@Service
public class PayPalSubscriptionService extends BaseService {

    public PayPalSubscriptionService(PaypalTokenService tokenService, WebClient webClient) {
        super(tokenService, webClient);
    }

    public PaypalCreateSubscriptionResponseBody paypalCreateSubscription(PaypalCreateSubscriptionRequestBody requestBody, String requestId) {
        String uri = baseSandboxUrl + "/v1/billing/subscriptions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("PayPal-Request-Id", requestId);
        headers.set("Prefer", "return=representation");

        return post(uri, headers, requestBody, PaypalCreateSubscriptionResponseBody.class);
    }

    public ListSubscriptionsResponseBody paypalListSubscriptions() {
        String uri = baseSandboxUrl + "/v1/billing/subscriptions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return get(uri, headers, ListSubscriptionsResponseBody.class);
    }

    public GetSubscriptionByIdResponseBody getSubscriptionById(String subscriptionId) {
        String uri = baseSandboxUrl + "/v1/billing/subscriptions/" + subscriptionId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return get(uri, headers, GetSubscriptionByIdResponseBody.class);
    }

    public Object paypalUpdateSubscription(String subscriptionId, UpdateSubscriptionRequestBody requestBody) {
        String uri = baseSandboxUrl + String.format("/v1/billing/subscriptions/%s", subscriptionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return post(uri, headers, requestBody, Object.class);
    }

    public Object paypalCancelSubscription(String subscriptionId, CancelSubscriptionRequestBody requestBody) {
        String uri = baseSandboxUrl + String.format("/v1/billing/subscriptions/%s/cancel", subscriptionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return post(uri, headers, requestBody, Object.class);
    }

    public ReviseSubscriptionResponseBody paypalReviseSubscription(String subscriptionId, ReviseSubscriptionRequestBody requestBody) {
        String uri = baseSandboxUrl + String.format("/v1/billing/subscriptions/%s/revise", subscriptionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return post(uri, headers, requestBody, ReviseSubscriptionResponseBody.class);
    }

    public Object paypalRefund(String captureId) {
        String uri = baseSandboxUrl + String.format("v2/payments/captures/%s/refund", captureId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return post(uri, headers, Object.class);
    }
}
