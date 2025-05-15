package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(RequestDTO .registerRequestDTO request) {
        return adminService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(RequestDTO.loginRequestDTO request) {
        return adminService.login(request);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String auth) {
        return adminService.loginGET(auth.substring(7));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String auth) {
        return adminService.logout(auth.substring(7));
    }

    @PostMapping("/addPlan")
    public ResponseEntity<?> addPlan(@ModelAttribute RequestDTO.planRequest request) {
        return adminService.addPlan(request);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return adminService.getAllUsers();
    }
    @GetMapping("/users/verify/{Id}")
    public ResponseEntity<?> getUser(@PathVariable Long Id) {
        return adminService.verifyUser(Id);
    }
    @GetMapping("/properties")
    public ResponseEntity<?> getProperties() {
        return adminService.getAllProperties();
    }
    @GetMapping("/payment-history")
    public ResponseEntity<?> getPaymentHistory() {
        return adminService.getAllPaymentHistory();
    }

    @DeleteMapping("/users/{Id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long Id) {
        return adminService.deleteUser(Id);
    }
    @DeleteMapping("/properties/{Id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long Id) {
        return adminService.deleteProperty(Id);
    }



}
