package com.pearl.propertiesApp.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Data
@Entity
public class PurchasedPlans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Plans plan;

    private boolean current;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
