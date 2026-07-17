package com.example.saastest.modules.payment.paypal.products.dto.response;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateProductResponseBody(
                String id,
                String name,
                String description,
                String type,
                String category,
                @JsonProperty("home_url") String homeUrl,
                String status,
                @JsonProperty("create_time") String createTime,
                @JsonProperty("update_time") String updateTime,
                List<LinkDto> links) {
}
