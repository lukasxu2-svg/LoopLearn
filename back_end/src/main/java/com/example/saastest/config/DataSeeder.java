package com.example.saastest.config;

import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.enums.PlanType;
import com.example.saastest.modules.plan.repository.PlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedDatabase(
            PlanRepository planRepository,
            BenutzerRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (planRepository.count() == 0) {

                Plan none = new Plan(PlanType.NONE, new BigDecimal("0.0"));
                Plan free = new Plan(PlanType.FREE, new BigDecimal("0.0"));
                Plan basic = new Plan(PlanType.BASIC, new BigDecimal("1.0"));
                Plan premium = new Plan(PlanType.PREMIUM, new BigDecimal("5.0"));


                planRepository.saveAll(List.of(none, free, basic, premium));
            }
        };
    }
}
