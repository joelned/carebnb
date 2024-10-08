package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Refugee {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int refugeeId;
    private String firstName;
    private String lastName;
    private int age;
    private String role;
    private boolean enabled;
    private String country;
    private String phoneNumber;
    @OneToMany(mappedBy="refugee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
