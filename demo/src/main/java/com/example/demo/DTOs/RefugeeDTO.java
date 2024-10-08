package com.example.demo.DTOs;

import com.example.demo.Models.Booking;
import lombok.Data;

import java.util.List;

@Data
public class RefugeeDTO{
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private String role;
    private boolean enabled;
    private String country;
    private String phoneNumber;
    private List<Booking> bookings;
}
