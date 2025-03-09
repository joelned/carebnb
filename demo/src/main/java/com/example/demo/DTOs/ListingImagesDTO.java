package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListingImagesDTO {
    private int houseListingImagesId;
    private int houseListingId;
    private String name;
    private String imagePath;
}
