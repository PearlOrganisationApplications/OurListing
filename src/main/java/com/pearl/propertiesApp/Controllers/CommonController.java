package com.pearl.propertiesApp.Controllers;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.PaymentHistory;
import com.pearl.propertiesApp.Entities.Users;
import com.pearl.propertiesApp.Repositories.PaymentHistoryRepository;
import com.pearl.propertiesApp.Services.CommonServices;
import com.pearl.propertiesApp.Services.PropertiesService;
import com.pearl.propertiesApp.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommonController {
    @Autowired
    private CommonServices services;
    @Autowired
    private UsersService usersService;
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private APIContext apiContext;
    @Autowired
    private PaymentHistoryRepository historyRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RequestDTO.registerRequestDTO request) {
        try {
            return services.register(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute RequestDTO.loginRequestDTO request) {
        try {
            return services.login(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String auth) {
        try {
            return services.loginGET(auth.substring(7));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String auth) {
        try {
            return services.logout(auth.substring(7));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestHeader("Authorization") String auth,
                                    @ModelAttribute RequestDTO.registerRequestDTO request) {
        try {
            return services.verifyOTP(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@ModelAttribute RequestDTO.registerRequestDTO request) {
        try {
            return services.resetPassword(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@ModelAttribute RequestDTO.registerRequestDTO request) {
        try {
            return services.sendOTP(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return usersService.getUseryById(id);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String auth,
                                        @ModelAttribute RequestDTO.updateUserRequest request) {
        try {
            return usersService.updateUser(auth.substring(7), request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String auth) {
        try {
            return usersService.deleteUser(auth.substring(7));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/properties")
    public ResponseEntity<?> getProperties() {
        return propertiesService.getAllProperties();
    }

    @GetMapping("/properties/{Id}")
    public ResponseEntity<?> getProperties(@PathVariable Long Id) {
        return propertiesService.getPropertyById(Id);
    }

    @GetMapping("/pay/success")
    public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId,
                                        @RequestParam("PayerID") String payerId) {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            // Validate state
            if (!"approved".equalsIgnoreCase(executedPayment.getState())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not approved.");
            }

            Transaction transaction = executedPayment.getTransactions().getFirst();
            String custom = transaction.getCustom();

            if (custom == null || !custom.matches("\\d+")) {
                return ResponseEntity.badRequest().body("Invalid user reference.");
            }

            Long userId = Long.valueOf(custom);
            Double amount = Double.parseDouble(transaction.getAmount().getTotal());
            String method = executedPayment.getPayer().getPaymentMethod();
            String txnId = executedPayment.getId();
            String state = executedPayment.getState();

            if (historyRepo.existsByTransactionId(txnId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Payment already processed.");
            }

            Users user = usersService.getUsersById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }


            PaymentHistory history = new PaymentHistory();
            history.setAmount(amount);
            history.setPaymentDate(LocalDateTime.now());
            history.setPaymentMethod(method);
            history.setStatus(state);
            history.setTransactionId(txnId);

            user.getPaymentHistory().add(history);
            usersService.saveUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("amount", amount);
            response.put("transactionId", txnId);
            response.put("status", state);
            response.put("method", method);

            return ResponseEntity.ok(response);

        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid amount or user ID format.");
        }
    }


    @GetMapping("/pay/cancel")
    public ResponseEntity<?> cancelPay(@RequestParam("token") String token) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "cancelled");
        response.put("token", token);
        response.put("message", "Payment was cancelled by the user.");
        return ResponseEntity.ok(response);
    }

}
