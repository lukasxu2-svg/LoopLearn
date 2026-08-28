package com.example.saastest.modules.payment.service;

import com.example.saastest.modules.payment.paypal.auth.dto.PaypalTokenResponse;
import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;

public abstract class BaseService {
    private final PaypalTokenService tokenService;

    protected String accessToken;

    protected Instant tokenExpiresIn;

    protected final WebClient webClient;

    @Value("${paypal.sandbox.base.url}")
    protected String baseSandboxUrl;

    public BaseService(PaypalTokenService tokenService, WebClient webClient) {
        this.tokenService = tokenService;
        this.webClient = webClient;
    }

    public <T> T get(String uri, HttpHeaders customHeaders, Class<T> responseType) {
        if (tokenIsExpired()) {
            refreshToken();
        }

        return webClient.get()
                .uri(uri)
                .headers(header -> {
                    header.setBearerAuth(accessToken);
                    header.addAll(customHeaders);
                })
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.createException()
                                .flatMap(Mono::error)
                )
                .bodyToMono(responseType)
                .block();
    }

    public <T> T post(String uri, HttpHeaders customHeaders, Object requestBody, Class<T> responseType) {
        if (tokenIsExpired()) {
            refreshToken();
        }
        
        return webClient.post()
                .uri(uri)
                .headers(header -> {
                    header.setBearerAuth(accessToken);
                    header.addAll(customHeaders);
                })
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    System.out.println("PayPal error: " + body);
                                    return Mono.error(new RuntimeException(body));
                                })
                )
                .bodyToMono(responseType)
                .block();
    }

    public <T> T post(String uri, HttpHeaders customHeaders, Class<T> responseType) {
        if (tokenIsExpired()) {
            refreshToken();
        }

        return webClient.post()
                .uri(uri)
                .headers(header -> {
                    header.setBearerAuth(accessToken);
                    header.addAll(customHeaders);
                })
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    System.out.println("PayPal error: " + body);
                                    return Mono.error(new RuntimeException(body));
                                })
                )
                .bodyToMono(responseType)
                .block();
    }

    private Boolean tokenIsExpired() {
        return accessToken == null
                || tokenExpiresIn == null
                || Instant.now().isAfter(tokenExpiresIn);
    }

    private void refreshToken() {
        PaypalTokenResponse response = tokenService.getAccessToken();
        accessToken = response.accessToken();
        tokenExpiresIn = Instant.now().plusSeconds(response.expiresIn());
    }

}
