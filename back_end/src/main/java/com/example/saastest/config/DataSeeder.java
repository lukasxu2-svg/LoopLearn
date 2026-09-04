package com.example.saastest.config;

import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.enums.PlanType;
import com.example.saastest.modules.plan.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {

    @Value("${paypal.product.id.free}")
    private String freeProductId;

    @Value("${paypal.plan.id.free}")
    private String freePlanId;

    @Value("${paypal.product.id.basic}")
    private String basicProductId;

    @Value("${paypal.plan.id.basic}")
    private String basicPlanId;

    @Value("${paypal.product.id.premium}")
    private String premiumProductId;

    @Value("${paypal.plan.id.premium}")
    private String premiumPlanId;


    @Bean
    CommandLineRunner seedDatabase(
            PlanRepository planRepository
    ) {
        return args -> {

            if (planRepository.count() == 0) {

                Plan free = new Plan(PlanType.FREE, new BigDecimal("0.0"), freePlanId, 1, freeProductId);
                Plan basic = new Plan(PlanType.BASIC, new BigDecimal("1.0"), basicPlanId, 2, basicProductId);
                Plan premium = new Plan(PlanType.PREMIUM, new BigDecimal("5.0"), premiumPlanId, 3, premiumProductId);


                planRepository.saveAll(List.of(free, basic, premium));
            }
        };
    }
}
