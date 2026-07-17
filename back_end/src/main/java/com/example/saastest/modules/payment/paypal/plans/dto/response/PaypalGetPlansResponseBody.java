package com.example.saastest.modules.payment.paypal.plans.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.example.saastest.modules.payment.paypal.plans.dto.PlanDto;

import java.util.List;

public record PaypalGetPlansResponseBody(
                List<PlanDto> plans,
                List<LinkDto> links) {
}
