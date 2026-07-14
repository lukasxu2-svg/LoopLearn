package com.example.saastest.modules.payment.paypal.plans.service;

import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import com.example.saastest.modules.payment.paypal.plans.dto.request.PaypalCreatePlanRequestBody;
import com.example.saastest.modules.payment.paypal.plans.dto.response.PaypalCreatePlanResponseBody;
import com.example.saastest.modules.payment.paypal.plans.dto.response.PaypalGetPlansResponseBody;
import com.example.saastest.modules.payment.service.BaseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.UUID;

@Service
public class PayPalPlanService extends BaseService {

    public PayPalPlanService(PaypalTokenService tokenService, WebClient webClient) {
        super(tokenService, webClient);
    }

    public PaypalCreatePlanResponseBody createPlan(PaypalCreatePlanRequestBody requestBody) {
        String uri = baseSandboxUrl + "/v1/billing/plans";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("PayPal-Request-Id", "PLAN-" + UUID.randomUUID());
        headers.set("Prefer", "return=representation");

        return post(uri, headers, requestBody, PaypalCreatePlanResponseBody.class);
    }

    public PaypalGetPlansResponseBody getPlans() {
        String uri = baseSandboxUrl + "/v1/billing/plans";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return get(uri, headers, PaypalGetPlansResponseBody.class);
    }
}
