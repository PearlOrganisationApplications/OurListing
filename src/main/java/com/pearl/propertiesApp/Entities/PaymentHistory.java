package com.pearl.propertiesApp.Entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Double amount;
    private LocalDateTime paymentDate;
    private String transactionId;
    private String paymentMethod;
    private String status;
}
