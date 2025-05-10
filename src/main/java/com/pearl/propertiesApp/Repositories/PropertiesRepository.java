package com.pearl.propertiesApp.Repositories;

import com.pearl.propertiesApp.Entities.Properties;
import com.pearl.propertiesApp.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {
    List<Properties> findByUser(Users user);

    Properties findByUserAndId(Users user, Long id);

    @Query("SELECT p FROM Properties p " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) " +
            "* cos(radians(p.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(p.latitude)))) < :radius " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) " +
            "* cos(radians(p.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(p.latitude)))) ASC")
    List<Properties> findPropertiesWithinRadius(double latitude, double longitude, double radius);
}