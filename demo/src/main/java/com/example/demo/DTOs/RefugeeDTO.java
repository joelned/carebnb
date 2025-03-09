package com.example.demo.DTOs;

import com.example.demo.Models.UserEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RefugeeDTO {
    private UserEntity userEntity;
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
