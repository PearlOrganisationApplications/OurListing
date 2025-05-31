package com.pearl.propertiesApp.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Plans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Double amount;
    private String planName;
    private String photo;
    private String description;
    private List<String> features;
    @JsonIgnore
    private Boolean enabled;
    private Integer duration;

}
