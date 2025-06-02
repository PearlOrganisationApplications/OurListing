package com.pearl.propertiesApp.Repositories;

import com.pearl.propertiesApp.Entities.Plans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlansRepository extends JpaRepository<Plans, Long> {

    List<Plans> findAllByEnabledTrue();

    Plans findByamount(Double amount);
}
