package com.example.saastest.modules.subscription.entity;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.plan.enums.PlanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "benutzer_id", unique = true)
    private Benutzer benutzerId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PlanType subType;

    @NotNull
    private BigDecimal cost;

    private SubscriptionStatusDto subStatus;

    @NotNull
    private String periodStart;

    @NotNull
    private String periodEnd;


    public Subscription() {
    }

    public Subscription(String periodEnd, String periodStart, SubscriptionStatusDto subStatus, BigDecimal cost, PlanType subType, Benutzer benutzerId) {
        this.periodEnd = periodEnd;
        this.periodStart = periodStart;
        this.subStatus = subStatus;
        this.cost = cost;
        this.subType = subType;
        this.benutzerId = benutzerId;
    }
}
