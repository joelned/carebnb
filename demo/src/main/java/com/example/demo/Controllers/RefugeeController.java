package com.example.demo.Controllers;

import com.example.demo.DTOs.DetailsDTO;
import com.example.demo.Models.HouseListing;
import com.example.demo.Models.UserEntity;
import com.example.demo.Repositories.ListingRepository;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.ListingService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.management.InstanceNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/apply")
public class RefugeeController {

    @Autowired
    private ListingService listingService;
    private final Logger logger = LoggerFactory.getLogger(RefugeeController.class);

    @PostMapping
    public String applyForListing(@RequestParam String name, LocalDate checkIn, LocalDate
            checkOut, HttpSession session){
        try {
            listingService.applyForListings(name, checkIn, checkOut, session);
            return "suc";
        }
        catch (Exception e){
           logger.error("Error: " + e.getMessage());
           return "details";
        }
    }

}
