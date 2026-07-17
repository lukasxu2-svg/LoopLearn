package com.example.saastest.modules.payment.paypal.subscriptions.dto.request;

import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionUpdateOperationDto;

public record UpdateSubscriptionRequestBody(
        SubscriptionUpdateOperationDto op,

        String path,

        Object value,

        String from) {
}
