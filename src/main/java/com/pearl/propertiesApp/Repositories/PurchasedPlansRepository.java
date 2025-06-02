package com.pearl.propertiesApp.Repositories;

import com.pearl.propertiesApp.Entities.PurchasedPlans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedPlansRepository extends JpaRepository<PurchasedPlans, Long> {
}
