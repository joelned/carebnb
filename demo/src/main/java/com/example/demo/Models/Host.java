package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table
@Data
public class Host {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int userId;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> room;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phoneNumber;
    private String role;
    private String location;
    private boolean enabled;
    @OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review>reviews;
}
