package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HouseListingImages {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int houseListingImagesId;
    @ManyToOne
    @JoinColumn(name = "house_listing_id")
    private HouseListing houseListing;
    private String imagePath;
}
