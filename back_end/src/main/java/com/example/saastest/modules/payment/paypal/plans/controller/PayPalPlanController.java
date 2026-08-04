package com.example.saastest.modules.payment.paypal.plans.controller;

import com.example.saastest.modules.payment.paypal.plans.dto.request.PaypalCreatePlanRequestBody;
import com.example.saastest.modules.payment.paypal.plans.dto.response.PaypalCreatePlanResponseBody;
import com.example.saastest.modules.payment.paypal.plans.dto.response.PaypalGetPlansResponseBody;
import com.example.saastest.modules.payment.paypal.plans.service.PayPalPlanService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal/plans")
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

    @GetMapping("/{id}")
    public void getPlanById(@PathVariable String id) {

    }
}
