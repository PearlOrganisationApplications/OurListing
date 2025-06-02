package com.pearl.propertiesApp.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    @JsonIgnore
    private String otp;

    @JsonIgnore
    private Boolean isVerified = false;

    @Column(unique = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
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
