package com.example.saastest.modules.payment.paypal.products.service;

import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import com.example.saastest.modules.payment.paypal.products.dto.request.CreateProductRequestBody;
import com.example.saastest.modules.payment.paypal.products.dto.response.CreateProductResponseBody;
import com.example.saastest.modules.payment.paypal.products.dto.response.GetProductsResponseBody;
import com.example.saastest.modules.payment.service.BaseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Collections;
import java.util.UUID;

@Service
public class PaypalProductsService extends BaseService {

    public PaypalProductsService(PaypalTokenService tokenService, WebClient webClient) {
        super(tokenService, webClient);
    }

    public GetProductsResponseBody getProducts() {
        String uri = baseSandboxUrl + "/v1/catalogs/products";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return get(uri, headers, GetProductsResponseBody.class);
    }

    public CreateProductResponseBody createProduct(CreateProductRequestBody requestBody) {
        String uri = baseSandboxUrl + "/v1/catalogs/products";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("PayPal-Request-Id", "PRODUCT-" + UUID.randomUUID());
        headers.set("Prefer", "return=representation");

        return post(uri, headers, requestBody, CreateProductResponseBody.class);
    }

}
