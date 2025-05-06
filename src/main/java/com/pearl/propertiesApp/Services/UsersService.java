package com.pearl.propertiesApp.Services;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.PaymentDetails;
import com.pearl.propertiesApp.Entities.PaymentHistory;
import com.pearl.propertiesApp.Entities.Properties;
import com.pearl.propertiesApp.Entities.Users;
import com.pearl.propertiesApp.Repositories.PropertiesRepository;
import com.pearl.propertiesApp.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PropertiesRepository propertiesRepository;

    public ResponseEntity<?> getUserById(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user.get());
    }

    public ResponseEntity<?> updateUser(String id, RequestDTO.updateUserRequest request) {
        Optional<Users> userOptional = usersRepository.findByToken(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Users user = userOptional.get();
        if (request.getName() != null) user.setName(request.getName());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getNumber() != null) {
            if (usersRepository.existsByNumberAndIsVerifiedTrue(request.getNumber())) {
                return ResponseEntity.badRequest().body("Phone number already registered");
            }
            user.setNumber(request.getNumber());
        }

        Users updatedUser = usersRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    public ResponseEntity<?> deleteUser(String id) {
        if (!usersRepository.existsByToken(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        usersRepository.delete(usersRepository.findByToken(id).orElseThrow(() ->
                new RuntimeException("User not Found")));
        return ResponseEntity.ok("User deleted successfully");
    }

    public ResponseEntity<?> addToFavorites(String token, Long propertyId) {
        Optional<Users> userOptional = usersRepository.findByToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        Properties property = propertiesRepository.findById(propertyId).orElseThrow(
                () -> new RuntimeException("Property not found")
        );

        Users user = userOptional.get();
        List<Properties> favorites = user.getFavorites();
        if (!favorites.contains(property)) {
            favorites.add(property);
        } else {
            favorites.remove(property);
        }
        user.setFavorites(favorites);

        usersRepository.save(user);
        return ResponseEntity.ok("Property added to favorites");
    }

    public ResponseEntity<?> getFavorites(String token) {
        Optional<Users> userOptional = usersRepository.findByToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Users user = userOptional.get();
        return ResponseEntity.ok(user.getFavorites());
    }

    public ResponseEntity<?> updatePaymentDetails(String token, PaymentDetails paymentDetails) {
        Optional<Users> userOptional = usersRepository.findByToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Users user = userOptional.get();
        user.setPaymentDetails(paymentDetails);
        usersRepository.save(user);
        return ResponseEntity.ok("Payment details updated successfully");
    }

    public ResponseEntity<?> addPaymentHistory(String token, PaymentHistory paymentHistory) {
        Optional<Users> userOptional = usersRepository.findByToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Users user = userOptional.get();
        user.getPaymentHistory().add(paymentHistory);
        usersRepository.save(user);
        return ResponseEntity.ok("Payment history added successfully");
    }

    public ResponseEntity<?> getPaymentHistory(String token) {
        Optional<Users> userOptional = usersRepository.findByToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Users user = userOptional.get();
        return ResponseEntity.ok(user.getPaymentHistory());
    }

    public ResponseEntity<?> getUseryById(Long id) {
        return ResponseEntity.ok(usersRepository.findById(id).orElseThrow(()->
                new RuntimeException("User not found")));
    }
}