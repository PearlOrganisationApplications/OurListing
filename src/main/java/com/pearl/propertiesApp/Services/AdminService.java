package com.pearl.propertiesApp.Services;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.Users;
import com.pearl.propertiesApp.Repositories.PaymentHistoryRepository;
import com.pearl.propertiesApp.Repositories.PropertiesRepository;
import com.pearl.propertiesApp.Repositories.UsersRepository;
import com.pearl.propertiesApp.Utilities.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;
    @Autowired
    private PropertiesRepository propertiesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<?> register(RequestDTO.registerRequestDTO request) {
        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsVerified(true);
        user.setToken(jwtTokenUtil.generateToken(user.getEmail(), "ADMIN"));
        user.setName(request.getName());
        user.setRole(Users.role.ADMIN);
        return ResponseEntity.ok(usersRepository.save(user));

    }

    public ResponseEntity<?> login(RequestDTO.loginRequestDTO request) {
        Users user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Users.role.ADMIN) {
            return ResponseEntity.badRequest().body("Invalid Credentials");
        }
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("Invalid Credentials");
        }
    }

    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    public ResponseEntity<?> verifyUser(Long id) {
        if (!usersRepository.existsById(id)) {
            return ResponseEntity.status(404).body("User not found");
        }
        Users user = usersRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not Found"));
        user.setIsVerified(!user.getIsVerified());
        usersRepository.save(user);
        return ResponseEntity.ok("User verified successfully");
    }

    public ResponseEntity<?> getAllProperties() {
        return ResponseEntity.ok(propertiesRepository.findAll());
    }

    public ResponseEntity<?> getAllPaymentHistory() {
        return ResponseEntity.ok(paymentHistoryRepository.findAll());
    }

    public ResponseEntity<?> deleteUser(Long id) {
        if (!usersRepository.existsById(id)) {
            return ResponseEntity.status(404).body("User not found");
        }
        usersRepository.delete(usersRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not Found")));
        return ResponseEntity.ok("User deleted successfully");
    }

    public ResponseEntity<?> deleteProperty(Long id) {
        if (!propertiesRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Property not found");
        }
        propertiesRepository.delete(propertiesRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Property not Found")));
        return ResponseEntity.ok("Property deleted successfully");
    }

    public ResponseEntity<?> logout(String token) {
        Users user = usersRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setToken(null);
        usersRepository.save(user);
        return ResponseEntity.ok("Logged out successfully");
    }

    public ResponseEntity<?> loginGET(String substring) {
        Users user = usersRepository.findByToken(substring)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Users.role.ADMIN) {
            return ResponseEntity.badRequest().body("Invalid Credentials");
        }
        return ResponseEntity.ok(user);
    }
}
