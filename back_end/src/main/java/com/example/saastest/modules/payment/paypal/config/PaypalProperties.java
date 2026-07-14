package com.example.saastest.modules.payment.paypal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaypalProperties {
    @Value("${paypal.client.id}")
    private String PAYPAL_CLIENT_ID;

    @Value("${paypal.client.secret}")
    private String PAYPAL_CLIENT_SECRET;

    public String getPAYPAL_CLIENT_ID() {
        return PAYPAL_CLIENT_ID;
    }

    public String getPAYPAL_CLIENT_SECRET() {
        return PAYPAL_CLIENT_SECRET;
    }

}
