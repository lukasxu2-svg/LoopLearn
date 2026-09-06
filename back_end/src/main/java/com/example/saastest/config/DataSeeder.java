package com.example.saastest.config;

import com.example.saastest.modules.benutzer.repository.BenutzerRepository;
import com.example.saastest.modules.plan.entity.Plan;
import com.example.saastest.modules.plan.enums.PlanType;
import com.example.saastest.modules.plan.repository.PlanRepository;
import com.example.saastest.modules.videos.dto.enums.LanguageType;
import com.example.saastest.modules.videos.entity.Video;
import com.example.saastest.modules.videos.repository.VideoRepository;
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
            PlanRepository planRepository,
            VideoRepository videoRepository
    ) {
        return args -> {

            if (planRepository.count() == 0) {

                Plan free = new Plan(PlanType.FREE, new BigDecimal("0.0"), freePlanId, 1, freeProductId);
                Plan basic = new Plan(PlanType.BASIC, new BigDecimal("1.0"), basicPlanId, 2, basicProductId);
                Plan premium = new Plan(PlanType.PREMIUM, new BigDecimal("5.0"), premiumPlanId, 3, premiumProductId);


                planRepository.saveAll(List.of(free, basic, premium));
            }
            // Seed videos
            if (videoRepository.count() == 0) {
                String videoPath = "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4";
                Video inheritance = new Video(1, "Inheritance", videoPath, LanguageType.JAVA, "04:20", "Learn how classes inherit behavior and data in Java.");
                Video streams = new Video(2, "Streams", videoPath, LanguageType.JAVA, "06:12", "Use functional-style data processing to work with collections.");
                Video microservices = new Video(3, "Microservices", videoPath, LanguageType.JAVA, "08:15", "Design service boundaries and resilient distributed systems.");
                Video variables = new Video(1, "Variables", videoPath, LanguageType.JAVASCRIPT, "03:36", "Learn about var, let, const, and scope in JavaScript.");
                Video domManipulation = new Video(3, "DOM Manipulation", videoPath, LanguageType.JAVASCRIPT, "07:14", "Interact with browser elements and update interfaces dynamically.");
                Video asyncAwait = new Video(2, "Async/Await", videoPath, LanguageType.JAVASCRIPT, "05:11", "Handle asynchronous workflows with cleaner syntax.");
                videoRepository.saveAll(List.of(inheritance, streams, microservices, variables, domManipulation, asyncAwait));
            }
        };
    }
}
