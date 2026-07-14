package com.example.saastest.modules.payment.paypal.plans.dto.response;

import com.example.saastest.modules.payment.paypal.dto.Link;
import com.example.saastest.modules.payment.paypal.plans.dto.Plan;

import java.util.List;

public record PaypalGetPlansResponseBody(
        List<Plan> plans,
        List<Link> links) {
}
