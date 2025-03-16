package com.example.demo.Repositories;

import com.example.demo.DTOs.ListingImagesDTO;
import com.example.demo.Models.HouseListing;
import com.example.demo.Models.HouseListingImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ListingImagesRepository extends JpaRepository<HouseListingImages, Integer> {

    @Query(
            "SELECT new com.example.demo.DTOs.ListingImagesDTO(li.houseListingImagesId, hl.houseListingId, " +
                    "hl.name, li.imagePath) " +
            "FROM HouseListingImages li " +
            "JOIN li.houseListing hl"
    )
    List<ListingImagesDTO> findListingImages();
    List<HouseListingImages> findByHouseListing_HouseListingId(UUID id);

    @Query("SELECT li FROM HouseListingImages li WHERE li.houseListing.booked = false")
    List<HouseListingImages>findByHouseListingBookedFalse();
}
