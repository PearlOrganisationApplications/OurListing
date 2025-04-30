package com.pearl.propertiesApp.Repositories;

import com.pearl.propertiesApp.Entities.Properties;
import com.pearl.propertiesApp.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {
    List<Properties> findByUser(Users user);
    Properties findByUserAndId(Users user, Long id);
}