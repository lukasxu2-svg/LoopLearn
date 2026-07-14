package com.example.saastest.modules.payment.paypal.auth.service;

import com.example.saastest.modules.payment.paypal.config.PaypalProperties;
import com.example.saastest.modules.payment.paypal.auth.dto.PaypalTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PaypalTokenService  {
    private final PaypalProperties properties;
    protected final WebClient webClient;

    public PaypalTokenService(PaypalProperties properties, WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    public PaypalTokenResponse getAccessToken() {
        String url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";

        PaypalTokenResponse response = webClient.post()
                .uri(url)
                .headers(headers -> {
                    headers.setBasicAuth(properties.getPAYPAL_CLIENT_ID(), properties.getPAYPAL_CLIENT_SECRET());
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(PaypalTokenResponse.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("PayPal returned an empty response.");
        }


        return response;
    }
}
