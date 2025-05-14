package com.pearl.propertiesApp.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String name;

    @Column(unique = true)
    private String number;

    private String address;
    private String token;
    private String otp;

    @JsonIgnore
    private Boolean isVerified = false;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column(unique = false)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PaymentHistory> paymentHistory;


    @JsonIgnore
    @ElementCollection
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Properties> favorites;

    @Enumerated(EnumType.STRING)
    private role role;

    public enum role {
        ADMIN,
        BUYER,
        OWNER,
        BROKER,
        LANDER
    }
}
