package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int reviewId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private Host user;
    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "bookingId")
    private Booking booking;
    private int rating;
    private String comments;
}
