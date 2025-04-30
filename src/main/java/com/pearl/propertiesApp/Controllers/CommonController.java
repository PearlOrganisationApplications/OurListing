package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Services.CommonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommonController {
    @Autowired
    private CommonServices services;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RequestDTO.registerRequestDTO request){
        return services.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute RequestDTO.loginRequestDTO request){
        return services.login(request);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String auth){
        return services.loginGET(auth.substring(7));
    }

}
