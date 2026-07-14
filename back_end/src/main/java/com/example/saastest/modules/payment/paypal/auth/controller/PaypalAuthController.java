package com.example.saastest.modules.payment.paypal.auth.controller;

import com.example.saastest.modules.payment.paypal.auth.dto.PaypalTokenResponse;
import com.example.saastest.modules.payment.paypal.auth.service.PaypalTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paypal/auth")
public class PaypalAuthController {

    private final PaypalTokenService service;

    public PaypalAuthController(PaypalTokenService service) {
        this.service = service;
    }

    @PostMapping("/token")
    public PaypalTokenResponse getToken() {
        return service.getAccessToken();
    }
}
