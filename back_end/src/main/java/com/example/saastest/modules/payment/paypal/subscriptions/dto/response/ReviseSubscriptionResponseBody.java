package com.example.saastest.modules.payment.paypal.subscriptions.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;

import java.util.List;

public record ReviseSubscriptionResponseBody(
        List<LinkDto> links
) {
}
