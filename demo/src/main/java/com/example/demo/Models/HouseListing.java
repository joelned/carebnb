package com.example.demo.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.util.ArrayList;
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
    @ManyToOne
    @JoinColumn(name = "host_id")
    private HostDetails hostDetails;
    private List<String> offerings;
    @OneToMany(mappedBy = "houseListing", cascade=CascadeType.ALL)
    private List<HouseListingImages> listingImages = new ArrayList<>();
}
