package com.example.demo.Repositories;


import com.example.demo.Models.HouseListing;
import com.example.demo.Models.RefugeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ListingRepository extends JpaRepository<HouseListing, Integer> {
    HouseListing findByName(String name);
}
