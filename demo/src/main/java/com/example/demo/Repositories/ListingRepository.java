package com.example.demo.Repositories;


import com.example.demo.Models.HostDetails;
import com.example.demo.Models.HouseListing;
import com.example.demo.Models.RefugeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ListingRepository extends JpaRepository<HouseListing, Integer> {
    HouseListing findByName(String name);
    List<HouseListing> findByHostDetails(HostDetails hostDetails);
}
