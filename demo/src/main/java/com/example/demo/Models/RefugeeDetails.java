package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class RefugeeDetails {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer refugeeDetailsId;
    @OneToOne
    @JoinColumn(name = "refugee_id")
    private UserEntity userEntity;
    private String profilePicturePath;
    private String name;
    private int age;
    private String email;
    private String gender;
    private int dependents;
    private String countryOfOrigin;
    private String countryOfFlee;
    private List<String> languagesSpoken;
    private String preferredLocation;
    private List<String> specialNeeds;
    private String selfDescription;
}
