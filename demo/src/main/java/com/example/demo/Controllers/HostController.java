package com.example.demo.Controllers;

import com.example.demo.Services.ListingService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@EnableCaching
@RequestMapping("/api/v1")
public class HostController {

    @Autowired
    private ListingService listingService;

   private final Logger logger = LoggerFactory.getLogger(HostController.class);

   @CacheEvict(value="homePageImages", allEntries= true)
    @PostMapping("/create-listing")
    public void uploadListing(
            HttpSession session,
            @RequestParam("name") String name,
            @RequestParam("maxGuests")int maxGuests,
            @RequestParam("description")String description,
            @RequestParam("address")String address,
            @RequestParam("offerings")List<String> offerings,
            @RequestParam("images[]")MultipartFile[] images
            )
            throws Exception {
        try{
            listingService.uploadListing(session, name, maxGuests, description, address, offerings, images);
            String username = (String)session.getAttribute("username");
            logger.info("{} created listing {} at {}", username, name, LocalDateTime.now());
        }
        catch(Exception ex){
            logger.error(ex.getMessage() + " " + LocalDateTime.now());
        }
    }


}
