package com.pearl.propertiesApp.Repositories;

import com.pearl.propertiesApp.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByNumber(String number);

    boolean existsByToken(String token);
    boolean existsByNumberAndIsVerifiedTrue(String number);

    Optional<Users> findByToken(String token);
}
