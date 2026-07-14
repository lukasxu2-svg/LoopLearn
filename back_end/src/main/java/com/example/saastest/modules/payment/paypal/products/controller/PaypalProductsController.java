package com.example.saastest.modules.payment.paypal.products.controller;

import com.example.saastest.modules.payment.paypal.products.create.dto.CreateOrderResponseBody;
import com.example.saastest.modules.payment.paypal.products.create.dto.CreateProductRequestBody;
import com.example.saastest.modules.payment.paypal.products.dto.response.GetProductsResponseBody;
import com.example.saastest.modules.payment.paypal.products.service.PaypalProductsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paypal/products")
public class PaypalProductsController {

    private final PaypalProductsService service;

    public PaypalProductsController(PaypalProductsService service) {
        this.service = service;
    }

    @PostMapping
    public CreateOrderResponseBody createProduct(@RequestBody CreateProductRequestBody requestBody) {
        return service.createProduct(requestBody);
    }

    @GetMapping
    public GetProductsResponseBody getProducts() {
        return service.getProducts();
    }
}
