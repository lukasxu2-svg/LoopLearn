package com.example.saastest.modules.payment.paypal.subscriptions.dto;

import java.time.Instant;
import java.util.List;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SubscriptionDto(
                String status,

                @JsonProperty("status_update_time") Instant statusUpdateTime,

                String id,

                @JsonProperty("plan_id") String planId,

                @JsonProperty("start_time") Instant startTime,

                SubscriberDto subscriber,

                @JsonProperty("create_time") Instant createTime,

                @JsonProperty("update_time") Instant updateTime,

                List<LinkDto> links) {

}
