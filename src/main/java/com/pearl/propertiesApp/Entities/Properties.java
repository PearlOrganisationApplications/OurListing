package com.pearl.propertiesApp.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Map;

@Data
@Entity
public class Properties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    private String info;

    @JsonManagedReference
    @JsonIgnore
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Enumerated(EnumType.STRING)
    private listType listingType;

    private Double price;
    private String location;
    private String landArea;

    private Double latitude;
    private Double longitude;

    @ElementCollection
    private List<String> photos;

    @Enumerated(EnumType.STRING)
    private type propertyType;

    @ElementCollection
    private Map<String,Integer> features;

    public enum listType{
        Rent,Sale,Lease
    }

    public enum type{
        Apartment,Villa,Plot
    }
}
