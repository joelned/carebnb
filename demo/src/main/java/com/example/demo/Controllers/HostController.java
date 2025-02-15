package com.example.demo.Controllers;

import com.example.demo.DTOs.ApprovalResponse;
import com.example.demo.DTOs.ErrorResponse;
import com.example.demo.DTOs.ListingDTO;
import com.example.demo.Services.ListingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class HostController {

    @Autowired
    private ListingService listingService;

   Logger logger = LoggerFactory.getLogger(HostController.class);

    @PostMapping("/upload-listing")
    public ResponseEntity<Object> uploadListing(@RequestBody ListingDTO listingDTO, Principal principal) throws Exception {
        try{
            listingService.uploadListing(listingDTO.getName(), listingDTO.getMaxGuests(),
                    listingDTO.getDescription(), listingDTO.getAddress(), listingDTO.getOfferings(),
                    listingDTO.getImages(), principal);
            ApprovalResponse response = new ApprovalResponse("Listing Uploaded Successfully",
                    true, LocalDateTime.now());
            logger.info("New Listing Uploaded By: {} at {}", principal.getName(), LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception ex){
            logger.error(ex.getMessage() + " " + LocalDateTime.now());
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

}
