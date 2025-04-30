package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.PaymentDetails;
import com.pearl.propertiesApp.Entities.PaymentHistory;
import com.pearl.propertiesApp.Services.CommonServices;
import com.pearl.propertiesApp.Services.PropertiesService;
import com.pearl.propertiesApp.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private PropertiesService propertiesService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RequestDTO.registerRequestDTO request) {
        return commonServices.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestDTO.loginRequestDTO request) {
        return commonServices.login(request);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginWithToken(@RequestHeader("Authorization") String auth) {
        return commonServices.loginGET(auth.substring(7));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return usersService.getUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody RequestDTO.updateUserRequest request) {
        return usersService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return usersService.deleteUser(id);
    }

    @GetMapping("/properties")
    public ResponseEntity<?> getProperties() {
        return propertiesService.getAllProperties();
    }

    @PostMapping("/favorites/{propertyId}")
    public ResponseEntity<?> addToFavorites(@RequestHeader("Authorization") String auth,
                                            @PathVariable Long propertyId) {
        return usersService.addToFavorites(auth.substring(7), propertyId);
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites(@RequestHeader("Authorization") String auth) {
        return usersService.getFavorites(auth.substring(7));
    }


}