package com.example.saastest.modules.subscription.entity;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import com.example.saastest.modules.subscription.enums.SubscriptionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Subscription {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Benutzer benutzer;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SubscriptionType subType;

    @NotNull
    private BigDecimal cost;
}
