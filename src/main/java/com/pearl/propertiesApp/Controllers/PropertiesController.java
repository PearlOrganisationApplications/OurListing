package com.pearl.propertiesApp.Controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.PaymentHistory;
import com.pearl.propertiesApp.Services.PropertiesService;
import com.pearl.propertiesApp.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/properties")
public class PropertiesController {
    Map<String, Double> plans = new HashMap<>(Map.of(
            "A", 500.00,
            "B", 1000.00,
            "C", 1500.00)
    );
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private APIContext apiContext;

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

    @PostMapping("/payment-history")
    public ResponseEntity<?> addPaymentHistory(@RequestHeader("Authorization") String auth,
                                               @ModelAttribute PaymentHistory paymentHistory) {
        return usersService.addPaymentHistory(auth.substring(7), paymentHistory);
    }

    @GetMapping("/payment-history")
    public ResponseEntity<?> getPaymentHistory(@RequestHeader("Authorization") String auth) {
        return usersService.getPaymentHistory(auth.substring(7));
    }

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(@RequestParam("plan") String plan,
                                           @RequestHeader("Authorization") String auth) {
        Payment payment = propertiesService.getPayment(plans.get(plan),
                usersService.getUserByToken(auth.substring(7)));

        try {
            Payment createdPayment = payment.create(apiContext);
            for (Links link : createdPayment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(Collections.singletonMap("redirect_url", link.getHref()));
                }
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Could not create payment");
    }

}