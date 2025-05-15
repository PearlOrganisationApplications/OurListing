package com.pearl.propertiesApp.Repositories;

import com.pearl.propertiesApp.Entities.Plans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlansRepository extends JpaRepository<Plans, Long> {

    List<Plans> findAllByEnabledTrue();
}
