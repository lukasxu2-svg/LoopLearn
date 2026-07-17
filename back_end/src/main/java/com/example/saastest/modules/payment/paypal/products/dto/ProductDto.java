package com.example.saastest.modules.payment.paypal.products.dto;

import com.example.saastest.modules.payment.paypal.dto.LinkDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProductDto(
                String id,
                String name,
                String description,
                List<LinkDto> links,
                @JsonProperty("create_time") String createTime) {
}
