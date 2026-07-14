package com.example.saastest.modules.payment.paypal.plans.dto.request;

import com.example.saastest.modules.payment.paypal.dto.Taxes;
import com.example.saastest.modules.payment.paypal.plans.dto.enums.PlanStatus;
import com.example.saastest.modules.payment.paypal.plans.dto.BillingCycle;
import com.example.saastest.modules.payment.paypal.plans.dto.PaymentPreferences;

import java.util.List;

public record PaypalCreatePlanRequestBody(
                String product_id,
                String name,
                String description,
                PlanStatus status,
                List<BillingCycle> billing_cycles,
                PaymentPreferences payment_preferences,
                Taxes taxes) {
}
