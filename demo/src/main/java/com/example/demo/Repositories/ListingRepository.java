package com.example.demo.Repositories;


import com.example.demo.Models.HostDetails;
import com.example.demo.Models.HouseListing;
import com.example.demo.Models.HouseListingImages;
import com.example.demo.Models.RefugeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<HouseListing, UUID> {
    HouseListing findByName(String name);
    List<HouseListing> findByHostDetails(HostDetails hostDetails);

    @Query("SELECT h FROM HouseListing h WHERE h.booked = false")
    List<HouseListing> findByBookedFalse();

}
