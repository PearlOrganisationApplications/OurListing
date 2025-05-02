package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.PaymentDetails;
import com.pearl.propertiesApp.Entities.PaymentHistory;
import com.pearl.propertiesApp.Services.PropertiesService;
import com.pearl.propertiesApp.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/properties")
public class PropertiesController {
    @Autowired
    private PropertiesService propertiesService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/add")
    public ResponseEntity<?> addProperties(@RequestHeader("Authorization") String auth,
                                           @ModelAttribute RequestDTO.propertyRequest request) {
        return propertiesService.addProperty(auth.substring(7), request);
    }

    @GetMapping
    public ResponseEntity<?> getProperties(@RequestHeader("Authorization") String auth) {
        return propertiesService.getProperties(auth.substring(7));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPropertyById(@RequestHeader("Authorization") String auth,
                                             @PathVariable Long id) {
        return propertiesService.getPropertyById(auth.substring(7), id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(@RequestHeader("Authorization") String auth,
                                            @PathVariable Long id,
                                            @ModelAttribute RequestDTO.propertyRequest request) {
        return propertiesService.updateProperty(auth.substring(7), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@RequestHeader("Authorization") String auth,
                                            @PathVariable Long id) {
        return propertiesService.deleteProperty(auth.substring(7), id);
    }

    @PutMapping("/payment-details")
    public ResponseEntity<?> updatePaymentDetails(@RequestHeader("Authorization") String auth,
                                                  @ModelAttribute PaymentDetails paymentDetails) {
        return usersService.updatePaymentDetails(auth.substring(7), paymentDetails);
    }

    @PostMapping("/payment-history")
    public ResponseEntity<?> addPaymentHistory(@RequestHeader("Authorization") String auth,
                                               @ModelAttribute PaymentHistory paymentHistory) {
        return usersService.addPaymentHistory(auth.substring(7), paymentHistory);
    }

    @GetMapping("/payment-history")
    public ResponseEntity<?> getPaymentHistory(@RequestHeader("Authorization") String auth) {
        return usersService.getPaymentHistory(auth.substring(7));
    }
}