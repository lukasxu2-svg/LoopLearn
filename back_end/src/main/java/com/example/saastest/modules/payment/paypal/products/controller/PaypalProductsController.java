package com.example.saastest.modules.payment.paypal.products.controller;

import com.example.saastest.modules.payment.paypal.products.dto.request.CreateProductRequestBody;
import com.example.saastest.modules.payment.paypal.products.dto.response.CreateProductResponseBody;
import com.example.saastest.modules.payment.paypal.products.dto.response.GetProductsResponseBody;
import com.example.saastest.modules.payment.paypal.products.service.PaypalProductsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal/products")
public class PaypalProductsController {

    private final PaypalProductsService service;

    public PaypalProductsController(PaypalProductsService service) {
        this.service = service;
    }

    @PostMapping
    public CreateProductResponseBody createProduct(@RequestBody CreateProductRequestBody requestBody) {
        return service.createProduct(requestBody);
    }

    @GetMapping
    public GetProductsResponseBody getProducts() {
        return service.getProducts();
    }
}
