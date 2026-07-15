package com.example.saastest.modules.benutzer.entity;

import com.example.saastest.modules.subscription.enums.SubscriptionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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

    @Enumerated(EnumType.STRING)
    private SubscriptionType subType = SubscriptionType.NONE;

    public Benutzer() {
    }

    public Benutzer(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public SubscriptionType getSubType() {
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
