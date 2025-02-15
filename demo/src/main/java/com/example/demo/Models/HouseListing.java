package com.example.demo.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class HouseListing {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int houseListingId;
    private String name;
    private String description;
    private int maxGuests;
    private String address;
    private double rating;
    private int noOfReviews;
    @ManyToOne
    @JoinColumn(name = "host_id")
    private HostDetails hostDetails;
    private List<String> offerings;
}
