package com.example.saastest.modules.payment.paypal.orders.controller;

import com.example.saastest.modules.payment.paypal.orders.dto.request.CreateOrderRequestBody;
import com.example.saastest.modules.payment.paypal.orders.dto.response.CreateOrderResponseBody;
import com.example.saastest.modules.payment.paypal.orders.service.PaypalOrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class PaymentOrderController {

    private final PaypalOrderService service;

    public PaymentOrderController(PaypalOrderService service) {
        this.service = service;
    }

    @PostMapping
    public CreateOrderResponseBody createOrder(@RequestBody CreateOrderRequestBody body) {
        return service.createOrder(body);
    }

    @PostMapping("/{orderId}/checkout")
    public String captureOrder(@PathVariable String orderId) {
        return service.captureOrder(orderId);
    }
}
