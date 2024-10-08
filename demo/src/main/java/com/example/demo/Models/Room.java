package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int roomId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private Host user;
    private String address;
    private String description;
    private int capacity;
    private boolean available;
    private String amenities;
}
