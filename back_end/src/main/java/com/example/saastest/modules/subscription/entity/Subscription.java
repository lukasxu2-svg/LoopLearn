package com.example.saastest.modules.subscription.entity;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.payment.paypal.subscriptions.dto.enums.SubscriptionStatusDto;
import com.example.saastest.modules.plan.enums.PlanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String paypalRequestId;

    private String subscriptionId;

    @ManyToOne
    @JoinColumn(name = "next_subscription")
    private Subscription nextSubscription;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "benutzer_id", nullable = false)
    private Benutzer benutzer;

    private String planId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionStatusDto subStatus;

    private Instant periodStart;

    private Instant periodEnd;

    private boolean canceled;

    public Subscription() {
    }

    public Subscription(String subscriptionId, String paypalRequestId, Benutzer benutzer, String planId,
                        PlanType planType, SubscriptionStatusDto subStatus, Instant periodStart, Instant periodEnd) {
        this.planType = planType;
        this.paypalRequestId = paypalRequestId;
        this.subscriptionId = subscriptionId;
        this.benutzer = benutzer;
        this.planId = planId;
        this.subStatus = subStatus;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public Subscription(Benutzer benutzer, PlanType planType, SubscriptionStatusDto subStatus) {
        this.planType = planType;
        this.benutzer = benutzer;
        this.subStatus = subStatus;
    }

    public Long getId() {
        return id;
    }

    public String getPaypalRequestId() {
        return paypalRequestId;
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

    public PlanType getPlanType() {
        return planType;
    }

    public Subscription getNextSubscription() {
        return nextSubscription;
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

    public void setCancelAll() {
        this.canceled = true;
        this.setSubStatus(SubscriptionStatusDto.CANCELLED);
        this.nextSubscription = null;
    }

    public void setNextSubscription(Subscription nextSubscription) {
        this.nextSubscription = nextSubscription;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", paypalRequestId='" + paypalRequestId + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                ", planId='" + planId + '\'' +
                ", planType=" + planType +
                ", subStatus=" + subStatus +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", canceled=" + canceled +
                '}';
    }
}
