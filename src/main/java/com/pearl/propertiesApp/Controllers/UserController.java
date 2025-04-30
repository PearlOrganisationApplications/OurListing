package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.RequestDTO;
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