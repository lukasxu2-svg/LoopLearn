package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import java.time.Instant;
import java.util.List;

import com.example.saastest.modules.payment.paypal.dto.Link;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Subscription(
        String status,

        @JsonProperty("status_update_time") Instant statusUpdateTime,

        String id,

        @JsonProperty("plan_id") String planId,

        @JsonProperty("start_time") Instant startTime,

        Subscriber subscriber,

        @JsonProperty("create_time") Instant createTime,

        @JsonProperty("update_time") Instant updateTime,

        List<Link> links) {

}
