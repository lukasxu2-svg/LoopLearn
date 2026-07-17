package com.example.saastest.modules.payment.paypal.plans.dto.request;

import com.example.saastest.modules.payment.paypal.dto.TaxesDto;
import com.example.saastest.modules.payment.paypal.plans.dto.enums.PlanStatusDto;
import com.example.saastest.modules.payment.paypal.plans.dto.BillingCycleDto;
import com.example.saastest.modules.payment.paypal.plans.dto.PaymentPreferences;

import java.util.List;

public record PaypalCreatePlanRequestBody(
        String product_id,
        String name,
        String description,
        PlanStatusDto status,
        List<BillingCycleDto> billing_cycles,
        PaymentPreferences payment_preferences,
        TaxesDto taxes) {
}
