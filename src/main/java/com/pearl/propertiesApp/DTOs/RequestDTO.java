package com.pearl.propertiesApp.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public class RequestDTO {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Getter
    @Setter
    public static class registerRequestDTO {
        private String email;
        private String password;
        private String name;
        private String number;
        private String address;
        private String role;
        private String Otp;
        private MultipartFile file;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Getter
    @Setter
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
    @Getter
    @Setter
    public static class updateUserRequest {
        private String name;
        private String number;
        private String address;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Getter
    @Setter
    public static class planRequest {
        private Double amount;
        private String planName;
        private MultipartFile photo;
        private String description;
        private List<String> features;
        private Boolean enabled;
        private Integer duration;

    }
}
