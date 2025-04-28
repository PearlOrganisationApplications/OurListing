package com.pearl.propertiesApp.DTOs;

import com.pearl.propertiesApp.Entities.Users;
import lombok.Data;

public class RequestDTO {
    @Data
    public static class registerRequestDTO{
        private String email;
        private String password;
        private String name;
        private String number;
        private String address;
        private String role;
    }
}
