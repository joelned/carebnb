package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DetailsDTO {
   private String name;
   private int age;
   private String email;
   private String gender;
   private int dependents;
   private String countryOfOrigin;
   private List<String> specialNeeds;
}
