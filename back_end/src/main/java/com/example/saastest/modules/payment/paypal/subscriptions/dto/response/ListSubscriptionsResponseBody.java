package com.example.saastest.modules.payment.paypal.subscriptions.dto.response;

import com.example.saastest.modules.payment.paypal.dto.Link;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.Subscription;
import java.util.List;

public record ListSubscriptionsResponseBody(
                List<Subscription> subscriptions,
                List<Link> links) {
}
