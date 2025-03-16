package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ListingImagesDTO {
    private int houseListingImagesId;
    private UUID houseListingId;
    private String name;
    private String imagePath;
}
