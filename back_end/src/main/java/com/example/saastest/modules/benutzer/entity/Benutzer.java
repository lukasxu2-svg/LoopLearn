package com.example.saastest.modules.benutzer.entity;

import com.example.saastest.modules.subscription.enums.SubscriptionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;


@Entity
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(max = 10)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Subscription Type is required")
    private SubscriptionType subType;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SubscriptionType getSubType() {
        return subType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubType(SubscriptionType subType) {
        this.subType = subType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
