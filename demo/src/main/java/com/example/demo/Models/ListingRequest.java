package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class ListingRequest {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private UUID listingRequestId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "refugee_id")
    private RefugeeDetails refugeeDetails;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "host_id")
    private HostDetails hostDetails;
    @OneToOne
    @JoinColumn(name = "house_listing_id")
    private HouseListing houseListing;
    private boolean approved;
}
