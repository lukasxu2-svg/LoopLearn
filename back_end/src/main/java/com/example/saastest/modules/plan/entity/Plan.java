package com.example.saastest.modules.plan.entity;

import com.example.saastest.modules.plan.enums.PlanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PlanType planType;

    @NotNull
    private BigDecimal monthlyPrice;

    @NotNull
    private Integer rank;

    @NotNull
    private String planId;

    @NotNull
    private String productId;

    public String getProductId() {
        return productId;
    }

    public Plan() {
    }

    public Plan(PlanType planType, BigDecimal monthlyPrice, String planId) {
        this.planType = planType;
        this.monthlyPrice = monthlyPrice;
        this.planId = planId;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public Long getId() {
        return id;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public String getPlanId() {
        return planId;
    }

    public Integer getRank() {
        return rank;
    }
}
