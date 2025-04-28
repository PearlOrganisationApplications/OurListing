package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Services.CommonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommonController {
    @Autowired
    private CommonServices services;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RequestDTO.registerRequestDTO request){
        return services.register(request);
    }
}
