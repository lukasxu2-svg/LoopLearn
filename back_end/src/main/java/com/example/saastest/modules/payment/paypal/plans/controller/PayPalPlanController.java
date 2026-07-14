package com.example.saastest.modules.payment.paypal.plans.controller;

import com.example.saastest.modules.payment.paypal.plans.dto.request.PaypalCreatePlanRequestBody;
import com.example.saastest.modules.payment.paypal.plans.dto.response.PaypalCreatePlanResponseBody;
import com.example.saastest.modules.payment.paypal.plans.dto.response.PaypalGetPlansResponseBody;
import com.example.saastest.modules.payment.paypal.plans.service.PayPalPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paypal/plans")
public class PayPalPlanController {

    private final PayPalPlanService service;

    public PayPalPlanController(PayPalPlanService service) {
        this.service = service;
    }

    @PostMapping
    public PaypalCreatePlanResponseBody createPlan(@RequestBody PaypalCreatePlanRequestBody requestBody) {
        return service.createPlan(requestBody);
    }

    @GetMapping
    public PaypalGetPlansResponseBody getPlans() {
        return service.getPlans();
    }
}
