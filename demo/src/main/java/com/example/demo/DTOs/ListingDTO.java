package com.example.demo.DTOs;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ListingDTO {
    private String name;
    private String description;
    private int maxGuests;
    private String address;
    private List<String> offerings;
    private List<MultipartFile>images;
}
