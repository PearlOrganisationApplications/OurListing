package com.pearl.propertiesApp.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public class RequestDTO {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    public static class registerRequestDTO {
        private String email;
        private String password;
        private String name;
        private String number;
        private String address;
        private String role;
        private String Otp;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    public static class loginRequestDTO {
        private String email;
        private String password;
        private String phone;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    public static class propertyRequest {
        private String title;
        private String info;
        private String listingType;

        private Double price;
        private String location;
        private String landArea;

        private Double latitude;
        private Double longitude;

        private List<MultipartFile> photos;
        private List<MultipartFile> documents;

        private String propertyType;

        private Map<String, Integer> features;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    public static class updateUserRequest {
        private String name;
        private String number;
        private String address;
    }
}
