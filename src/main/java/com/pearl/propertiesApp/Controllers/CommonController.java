package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Services.CommonServices;
import com.pearl.propertiesApp.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommonController {
    @Autowired
    private CommonServices services;
    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RequestDTO.registerRequestDTO request) {
        return services.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute RequestDTO.loginRequestDTO request) {
        return services.login(request);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String auth) {
        return services.loginGET(auth.substring(7));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String auth,
                                        @RequestBody RequestDTO.updateUserRequest request) {
        return usersService.updateUser(auth.substring(7), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String auth) {
        return usersService.deleteUser(auth.substring(7));
    }

}
