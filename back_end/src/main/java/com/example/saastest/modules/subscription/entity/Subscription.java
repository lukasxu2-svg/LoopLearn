package com.example.saastest.modules.subscription.entity;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.plan.enums.PlanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String subscriptionId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "benutzer_id", nullable = false)
    private Benutzer benutzer;

    @NotNull
    private String planId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionStatusDto subStatus;

    @NotNull
    private Instant periodStart;

    @NotNull
    private Instant periodEnd;

    private boolean canceled;


    public Subscription() {
    }

    public Subscription(String subscriptionId, Benutzer benutzer, String planId, SubscriptionStatusDto subStatus, Instant periodStart, Instant periodEnd) {
        this.subscriptionId = subscriptionId;
        this.benutzer = benutzer;
        this.planId = planId;
        this.subStatus = subStatus;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public Long getId() {
        return id;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public String getPlanId() {
        return planId;
    }

    public SubscriptionStatusDto getSubStatus() {
        return subStatus;
    }

    public Instant getPeriodStart() {
        return periodStart;
    }

    public Instant getPeriodEnd() {
        return periodEnd;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setSubStatus(SubscriptionStatusDto subStatus) {
        this.subStatus = subStatus;
    }

    public void setCanceled() {
        this.canceled = true;
    }
}
