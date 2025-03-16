package com.example.demo.Controllers;

import com.example.demo.Services.ListingService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/apply")
public class RefugeeController {

    @Autowired
    private ListingService listingService;
    private final Logger logger = LoggerFactory.getLogger(RefugeeController.class);

    @PreAuthorize("hasAuthority('SCOPE_REFUGEE')")
    @PostMapping
    public String applyForListing(@RequestParam String name, LocalDate checkIn, LocalDate
            checkOut, HttpSession session){
        try {
            listingService.applyForListings(name, checkIn, checkOut, session);
            return "suc";
        }
        catch (Exception e){
           logger.error("Error: " + e.getMessage());
           return "redirect:/home";
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedHandler(RedirectAttributes attributes){
        attributes.addFlashAttribute("error", "You cannot reserve an apartment as you are a host");
        return "redirect:/details";
    }

}
