package com.example.saastest.modules.subscription.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;

import java.util.List;

public record CreateSubscriptionResponseBody(
        List<LinkDto> links
) {
}
