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

    public Plan() {
    }

    public Plan(PlanType planType, BigDecimal monthlyPrice) {
        this.id = id;
        this.planType = planType;
        this.monthlyPrice = monthlyPrice;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }
}
