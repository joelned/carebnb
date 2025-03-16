package com.example.demo.Controllers;

import com.example.demo.Models.HouseListing;
import com.example.demo.Models.HouseListingImages;
import com.example.demo.Models.ListingRequest;
import com.example.demo.Repositories.*;
import com.example.demo.Services.ListingService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
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


    @PostMapping("/approve-request")
    public String approveRequest(HttpSession session, @RequestParam("listingRequestId") UUID listingRequestId){
        try{
            String username = (String)session.getAttribute("username");
            listingService.approveListing(listingRequestId);
            ListingRequest request = listingRequestRepository.findByListingRequestId(listingRequestId);
            HouseListing listing = request.getHouseListing();
            listing.setBooked(true);
            listingRepository.save(listing);
            logger.info("Listing Request approved by HOST {}", username);

            listingService.sendMail(session, listingRequestId);
            logger.info("Email Sent at {}", LocalDateTime.now());

            return "redirect:/clients";
        }
        catch(Exception ex){
            return "redirect:/clients";
        }
    }

    @PostMapping("/decline-request")
    public String declineRequest(HttpSession session, @RequestParam("refugeeName") String refugeeName,
    @RequestParam("listingRequestId") UUID listingRequestId) {
        try{
           String username = (String)session.getAttribute("username");
           listingService.declineRequest(listingRequestId);

           logger.info("Listing Request declined by HOST {}", username);

           listingService.sendDeclineMail(session, listingRequestId, refugeeName);
           return "redirect:/clients";
        }
        catch(Exception ex){
            logger.error("Error declining request {}", ex.getMessage());
            return "redirect:/clients";
        }
    }


    @GetMapping("/listing/{id}")
    public String getListing(@PathVariable UUID id, RedirectAttributes attributes){
        try{
            List<HouseListingImages> imagesForListing = listingService.getSpecificListingImages(id);
            Optional<HouseListing> houseListing = listingRepository.findById(id);

            if(houseListing.isEmpty()){
                throw new Exception("Listing not found");
            }
            attributes.addFlashAttribute("imagesForListing", imagesForListing);
            attributes.addFlashAttribute("houseListing", houseListing);

            return "redirect:/listing-details";
        }catch(Exception ex){
            return "redirect:/home";
        }
    }
}
