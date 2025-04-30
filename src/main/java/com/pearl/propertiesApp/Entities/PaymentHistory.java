package com.pearl.propertiesApp.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
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
