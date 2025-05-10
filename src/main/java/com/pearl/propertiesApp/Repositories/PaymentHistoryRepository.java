package com.pearl.propertiesApp.Repositories;

import com.pearl.propertiesApp.Entities.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
