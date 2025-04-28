package com.pearl.propertiesApp.Services;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.Users;
import com.pearl.propertiesApp.Repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CommonServices {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public ResponseEntity<?> register(RequestDTO.registerRequestDTO request) {

        Users user =usersRepository.findByEmail(request.getEmail()).orElse(new Users());

        if(usersRepository.existsByNumberAndIsVerifiedTrue(request.getNumber()))
            return ResponseEntity.badRequest().body("Phone Number is Registered to Another User");

        if(user.getIsVerified()) return ResponseEntity.badRequest().body("User Already Exists");

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setNumber(request.getNumber());
        user.setAddress(request.getAddress());
        user.setRole(Users.role.valueOf(request.getRole()));
        return ResponseEntity.ok(usersRepository.save(user));
    }
}
