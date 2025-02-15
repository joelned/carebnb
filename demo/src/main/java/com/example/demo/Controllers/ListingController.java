package com.example.demo.Controllers;

import com.example.demo.DTOs.ApprovalResponse;
import com.example.demo.DTOs.ErrorResponse;
import com.example.demo.Repositories.*;
import com.example.demo.Services.ListingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class ListingController {
    
    @Autowired
    private ListingRequestRepository requestRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    ListingRequestRepository listingRequestRepository;
    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private RefugeeRepository refugeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListingRepository listingRepository;
    private final Logger logger = LoggerFactory.getLogger(ListingController.class);
    @Autowired
    private ListingService listingService;

    @GetMapping(path = "/requests")
    public ResponseEntity<?> getListingRequests(Principal principal) {
        try {
            return new ResponseEntity<>(listingService.getListingRequests(principal), HttpStatus.OK);
        }
        catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/approve-request")
    public ResponseEntity<Object>approveRequest(Principal principal, @RequestParam("listingRequestId") int listingRequestId){
        try{
            listingService.approveListing(principal, listingRequestId);
            logger.info("Listing Request approved by HOST {}", principal.getName());
            listingService.sendMail(principal, listingRequestId);
            ApprovalResponse approvalResponse = new ApprovalResponse("Request Accepted",
                    true, LocalDateTime.now());
            return ResponseEntity.ok(approvalResponse);
        }
        catch(Exception ex){
            logger.info("Attempt to approve listing request failed: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
