package com.example.demo.DTOs;

import com.example.demo.Models.Review;
import com.example.demo.Models.Room;
import lombok.Data;

import java.util.List;

@Data
public class HostDTO {
    private int userId;
    private List<Room> room;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phoneNumber;
    private String role;
    private String location;
    private boolean enabled;
    private List<Review>reviews;
}
