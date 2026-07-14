package com.example.saastest.modules.payment.paypal.subscriptions.dto.request;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionUpdateOperation;

public record UpdateSubscriptionRequestBody(
                SubscriptionUpdateOperation op,

                String path,

                Object value,

                String from) {
}
