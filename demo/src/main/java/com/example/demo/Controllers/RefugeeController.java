package com.example.demo.Controllers;

import com.example.demo.DTOs.DetailsDTO;
import com.example.demo.Models.HouseListing;
import com.example.demo.Models.UserEntity;
import com.example.demo.Repositories.ListingRepository;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.ListingService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.management.InstanceNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RefugeeController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefugeeRepository refugeeRepository;
    @Autowired
    private ListingService listingService;

    @Autowired
    private ListingRepository listingRepository;

    private final Logger logger = LoggerFactory.getLogger(RefugeeController.class);

    @Transactional
    @PostMapping("/apply")
    public ResponseEntity<String>applyForListing(@RequestParam String name, Principal principal) throws InstanceNotFoundException {
        try {
            listingService.applyForListings(name, principal);
            return new ResponseEntity<>("Listing Applied For Successfully", HttpStatus.OK);
        }
        catch (Exception e){
           logger.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Error applying for listing", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<List<DetailsDTO>>getRefugeeDetails(Principal principal){
        UserEntity user = userRepository.findByUsername(principal.getName());
        List<DetailsDTO> refugeeDetails = refugeeRepository.findRefugeeByUserEntity(user);
        return new ResponseEntity<>(refugeeDetails, HttpStatus.OK);
    }

    @GetMapping("/listing")
    public ResponseEntity<List<HouseListing>>getListing(){
        List<HouseListing> listings = listingRepository.findAll();
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }

}
