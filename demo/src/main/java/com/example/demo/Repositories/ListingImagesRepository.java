package com.example.demo.Repositories;

import com.example.demo.Models.HouseListingImages;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ListingImagesRepository extends JpaRepository<HouseListingImages, Integer> {

}
