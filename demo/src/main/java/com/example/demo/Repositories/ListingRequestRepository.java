package com.example.demo.Repositories;

import com.example.demo.DTOs.RefugeeProjection;
import com.example.demo.Models.HostDetails;
import com.example.demo.Models.HouseListing;
import com.example.demo.Models.ListingRequest;
import com.example.demo.Models.RefugeeDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ListingRequestRepository extends JpaRepository<ListingRequest, Integer> {
    ListingRequest findFirstByRefugeeDetailsAndApprovedTrue(RefugeeDetails refugeeDetails);

    @Modifying
    @Transactional
    @Query("UPDATE ListingRequest lr SET lr.approved = true WHERE lr.listingRequestId = :listingRequestId")
    void approveListingRequest(@Param("listingRequestId")int listingRequestId);

    @Query("SELECT new com.example.demo.DTOs.RefugeeProjection(r.name, lr.checkIn, lr.checkOut, r.age, r.email, r.gender, r.dependents, " +
            "r.countryOfOrigin, r.countryOfFlee, r.preferredLocation, r.specialNeeds, r.selfDescription," +
            "hl.name) " +
            "FROM ListingRequest lr " +
            "JOIN lr.refugeeDetails r " +
            "JOIN lr.houseListing hl " +
            "WHERE lr.hostDetails = :hostDetails")
    List<RefugeeProjection> findRefugeeDetailsByHostDetails(@Param("hostDetails") HostDetails hostDetails);

    ListingRequest findByListingRequestId(int listingRequestId);
}
