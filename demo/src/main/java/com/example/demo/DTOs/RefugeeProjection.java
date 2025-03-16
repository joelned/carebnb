package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class RefugeeProjection {
    private String name;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int age;
    private String email;
    private String gender;
    private int dependents;
    private String countryOfOrigin;
    private String countryOfFlee;
    private String preferredLocation;
    private List<String> specialNeeds;
    private String selfDescription;
    private String houseListingName;

    public RefugeeProjection(String name, int age, String email, String gender, int dependents, String countryOfOrigin, List<String>specialNeeds){
        this.name = name;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.dependents = dependents;
        this.countryOfOrigin = countryOfOrigin;
        this.specialNeeds = specialNeeds;
    }
}
