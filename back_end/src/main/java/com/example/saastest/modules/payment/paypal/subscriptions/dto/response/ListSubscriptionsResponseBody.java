package com.example.saastest.modules.payment.paypal.subscriptions.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.SubscriptionDto;
import java.util.List;

public record ListSubscriptionsResponseBody(
        List<SubscriptionDto> subscriptions,
        List<LinkDto> links) {
}
