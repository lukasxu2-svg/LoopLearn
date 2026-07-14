package com.example.saastest.modules.payment.paypal.orders.service;

import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import com.example.saastest.modules.payment.paypal.orders.dto.request.CreateOrderRequestBody;
import com.example.saastest.modules.payment.paypal.orders.dto.response.CreateOrderResponseBody;
import com.example.saastest.modules.payment.service.BaseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PaypalOrderService extends BaseService {

    public PaypalOrderService(PaypalTokenService tokenService, WebClient webClient) {
        super(tokenService, webClient);
    }

    public CreateOrderResponseBody createOrder(CreateOrderRequestBody requestBody) {

        String uri = "https://api-m.sandbox.paypal.com/v2/checkout/orders";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return post(uri, headers, requestBody, CreateOrderResponseBody.class);
    }

    public String captureOrder(String orderId) {
        String url = baseSandboxUrl + String.format("/v2/checkout/orders/%s/capture", orderId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return post(url, headers, String.class);
    }
}
