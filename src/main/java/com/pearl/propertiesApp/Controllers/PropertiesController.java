package com.pearl.propertiesApp.Controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.pearl.propertiesApp.Configurations.PaypalConfig;
import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.PaymentHistory;
import com.pearl.propertiesApp.Entities.Plans;
import com.pearl.propertiesApp.Entities.PurchasedPlans;
import com.pearl.propertiesApp.Entities.Users;
import com.pearl.propertiesApp.Repositories.PlansRepository;
import com.pearl.propertiesApp.Services.PropertiesService;
import com.pearl.propertiesApp.Services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertiesController {

    private final PropertiesService propertiesService;
    private final UsersService usersService;
    private final PaypalConfig paypalConfig;
    private final PlansRepository plansRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addProperties(@RequestHeader("Authorization") String auth,
                                           @ModelAttribute RequestDTO.propertyRequest request) throws IOException {
        log.info("request data :{}", request.toString());
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

    @GetMapping("/plans")
    public ResponseEntity<?> getPlans() {
        return ResponseEntity.ok(plansRepository.findAllByEnabledTrue());
    }

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(@RequestParam("plan") String plan,
                                           @RequestHeader("Authorization") String auth) {
        List<Plans> allPlans = plansRepository.findAllByEnabledTrue();
        Map<String, Double> plans = allPlans.stream()
                .collect(Collectors.toMap(Plans::getPlanName, Plans::getAmount));
        Users user=usersService.getUserByToken(auth.substring(7));

        if (plans.isEmpty()) {
            plans = new HashMap<>(Map.of(
                    "A", 500.00,
                    "B", 1000.00,
                    "C", 1500.00
            ));
        }

        Payment payment = propertiesService.getPayment(plans.get(plan),user);
        if(payment == null) {
            PaymentHistory history = new PaymentHistory();
            history.setAmount(plans.get(plan));
            history.setPaymentDate(LocalDateTime.now());
            history.setPaymentMethod("NONE");
            history.setStatus("PAID");
            history.setTransactionId(UUID.randomUUID().toString());

            List<PurchasedPlans> purchasedPlansList = user.getPurchasedPlans() != null ? user.getPurchasedPlans() : new ArrayList<>();
            PurchasedPlans purchasedPlans = new PurchasedPlans();
            Plans userPlan = plansRepository.findByamount(history.getAmount());

            purchasedPlans.setPlan(userPlan);

            LocalDateTime startDate;
            if (!purchasedPlansList.isEmpty() && purchasedPlansList.getLast().getEndDate() != null) {
                startDate = purchasedPlansList.getLast().getEndDate().plusDays(1);
            } else {
                startDate = LocalDateTime.now();
            }
            purchasedPlans.setStartDate(startDate);

            purchasedPlans.setEndDate(purchasedPlans.getStartDate().plusDays(userPlan.getDuration()));
            purchasedPlans.setCurrent(LocalDateTime.now().isAfter(purchasedPlans.getStartDate()));
            purchasedPlansList.add(purchasedPlans);
            //SAVE URSELF
            user.getPaymentHistory().add(history);
            user.setPurchasedPlans(purchasedPlansList);

            usersService.saveUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("amount", history.getAmount());
            response.put("transactionId", history.getTransactionId());
            response.put("status", history.getStatus());
            response.put("method", history.getPaymentMethod());

            return ResponseEntity.ok(response);
        }

        try {
            APIContext freshContext = paypalConfig.getAPIContext();
            freshContext.addHTTPHeader("PayPal-Request-Id", UUID.randomUUID().toString());
            Payment createdPayment = payment.create(freshContext);
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