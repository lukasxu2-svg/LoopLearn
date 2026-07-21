package com.example.saastest.modules.benutzer.entity;

import com.example.saastest.modules.plan.enums.PlanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 30)
    private String firstname;

    @NotBlank(message = "Last name is required")
    @Size(max = 30)
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlanType subType = PlanType.NONE;

    public Benutzer() {
    }

    public Benutzer(String firstname, String lastname, String email, String password,
                    PlanType subscriptionType) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.subType = subscriptionType;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public PlanType getSubType() {
        return subType;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLastname() {
        return lastname;
    }
}
