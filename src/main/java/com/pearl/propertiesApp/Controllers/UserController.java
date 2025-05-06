package com.pearl.propertiesApp.Controllers;

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
    private PropertiesService propertiesService;


    @GetMapping("/properties")
    public ResponseEntity<?> getProperties() {
        return propertiesService.getAllProperties();
    }

    @GetMapping("/properties/{Id}")
    public ResponseEntity<?> getProperties(@PathVariable Long Id) {
        return propertiesService.getPropertyById(Id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return usersService.getUseryById(id);
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