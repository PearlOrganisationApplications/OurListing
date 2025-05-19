package com.pearl.propertiesApp.Services;

import com.paypal.api.payments.*;
import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.Properties;
import com.pearl.propertiesApp.Entities.Users;
import com.pearl.propertiesApp.Repositories.PropertiesRepository;
import com.pearl.propertiesApp.Repositories.UsersRepository;
import com.pearl.propertiesApp.Utilities.CloudinaryService;
import com.pearl.propertiesApp.Utilities.FileStackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PropertiesService {
    @Value("${baseurl.web}")
    private String web;

    @Autowired
    private PropertiesRepository propertiesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private FileStackService fileStackService;

    public ResponseEntity<?> addProperty(String token, RequestDTO.propertyRequest request) throws IOException {
//        try {

        log.info("request data :{}", request);
        Optional<Users> userOptional = usersRepository.findByToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Users user = userOptional.get();

        Properties property = new Properties();
        property.setTitle(request.getTitle());
        property.setInfo(request.getInfo());
        property.setUser(user);
        property.setListingType(Properties.listType.valueOf(request.getListingType()));
        property.setPrice(request.getPrice());
        property.setLocation(request.getLocation());
        property.setLandArea(request.getLandArea());
        property.setLatitude(request.getLatitude());
        property.setLongitude(request.getLongitude());

        List<String> photoList = new ArrayList<>();
        if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
            for (MultipartFile photo : request.getPhotos()) {
                String uid = String.valueOf(UUID.randomUUID());
                cloudinaryService.uploadFile(photo, uid);
                photoList.add(cloudinaryService.getPhotoUrl(uid));
            }
        } else {
            return ResponseEntity.badRequest().body("Photos not Found");
        }
        property.setPhotos(photoList);

        List<String> documentList = new ArrayList<>();
        if (request.getDocuments() != null && !request.getDocuments().isEmpty()) {
            for (MultipartFile document : request.getDocuments()) {
                documentList.add(fileStackService.uploadFile(document));
            }
        } else {
            return ResponseEntity.badRequest().body("Documents not Found");
        }
        property.setDocuments(documentList);

        property.setPropertyType(Properties.type.valueOf(request.getPropertyType()));
        property.setFeatures(request.getFeatures());

        Properties savedProperty = propertiesRepository.save(property);
        return ResponseEntity.ok(savedProperty);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error adding property: " + e.getMessage());
//        }
    }

    public ResponseEntity<?> getProperties(String token) {
        try {
            Optional<Users> userOptional = usersRepository.findByToken(token);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            Users user = userOptional.get();
            List<Properties> properties = propertiesRepository.findByUser(user);
            return ResponseEntity.ok(properties);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching properties: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getPropertyById(String token, Long id) {
        try {
            Optional<Users> userOptional = usersRepository.findByToken(token);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            Users user = userOptional.get();
            Properties property = propertiesRepository.findByUserAndId(user, id);
            if (property == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
            }
            return ResponseEntity.ok(property);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching property: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getPropertyById(Long id) {
        try {
            Properties property = propertiesRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Property not found"));
            if (property == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
            }
            return ResponseEntity.ok(property);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching property: " + e.getMessage());
        }
    }

    public ResponseEntity<?> updateProperty(String token, Long id, RequestDTO.propertyRequest request) {
        try {
            Optional<Users> userOptional = usersRepository.findByToken(token);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            Users user = userOptional.get();
            Properties existingProperty = propertiesRepository.findByUserAndId(user, id);
            if (existingProperty == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
            }

            existingProperty.setTitle(request.getTitle());
            existingProperty.setInfo(request.getInfo());
            existingProperty.setListingType(Properties.listType.valueOf(request.getListingType()));
            existingProperty.setPrice(request.getPrice());
            existingProperty.setLocation(request.getLocation());
            existingProperty.setLandArea(request.getLandArea());
            existingProperty.setLatitude(request.getLatitude());
            existingProperty.setLongitude(request.getLongitude());
            List<String> photoList = new ArrayList<>();
            if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
                for (MultipartFile photo : request.getPhotos()) {
                    String uid = String.valueOf(UUID.randomUUID());
                    cloudinaryService.uploadFile(photo, uid);
                    photoList.add(cloudinaryService.getPhotoUrl(uid));
                }
                existingProperty.setPhotos(photoList);
            }
            existingProperty.setPropertyType(Properties.type.valueOf(request.getPropertyType()));
            existingProperty.setFeatures(request.getFeatures());

            Properties updatedProperty = propertiesRepository.save(existingProperty);
            return ResponseEntity.ok(updatedProperty);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating property: " + e.getMessage());
        }
    }

    public ResponseEntity<?> deleteProperty(String token, Long id) {
        try {
            Optional<Users> userOptional = usersRepository.findByToken(token);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            Users user = userOptional.get();
            Properties property = propertiesRepository.findByUserAndId(user, id);
            if (property == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
            }

            propertiesRepository.delete(property);
            return ResponseEntity.ok("Property deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting property: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getAllProperties() {
        return ResponseEntity.ok(propertiesRepository.findAll());
    }

    public ResponseEntity<?> getPropertyByRadius(Double lat, Double longi, Double rad) {
        return ResponseEntity.ok().body(propertiesRepository.findPropertiesWithinRadius(lat, longi, rad));
    }


    public Payment getPayment(double sum, Users user) {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", sum));

        Transaction transaction = new Transaction();
        transaction.setDescription("Payment description");
        transaction.setAmount(amount);
        transaction.setCustom(String.valueOf(user.getId()));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(web + "/api/pay/cancel");
        redirectUrls.setReturnUrl(web + "/api/pay/success");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);
        return payment;
    }

}