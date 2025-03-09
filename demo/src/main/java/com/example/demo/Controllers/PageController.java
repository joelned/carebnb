package com.example.demo.Controllers;

import com.example.demo.Models.*;
import com.example.demo.Repositories.HostRepository;
import com.example.demo.Repositories.ListingImagesRepository;
import com.example.demo.Repositories.ListingRepository;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Services.ListingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class PageController implements ErrorController {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private RefugeeRepository refugeeRepository;
    @Autowired
    private ListingImagesRepository imagesRepository;

    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private ListingService listingService;
    @GetMapping("/")
    public String root(){
        return "redirect:/login";
    }
    @GetMapping("/signup")
    public String index(){
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            String role = (String)session.getAttribute("role");
            List<HouseListingImages> listingImages = imagesRepository.findAll();
            List<HouseListing> listings = listingRepository.findAll();
            model.addAttribute("houseListings", listings);
            model.addAttribute("listingImages", listingImages);
            model.addAttribute("role", role);
            return "home";
        }
        return "redirect:/login";
    }
    @GetMapping("/signup/{role}")
    public String signup(@PathVariable String role, Model model){
        model.addAttribute("role", role);
        if(role.equals("refugee")){
            return "refugee-signup";
        }
        else{
            return "sign";
        }
    }

    @GetMapping("/get-started")
    public String getStarted(HttpSession httpSession){
        Boolean registered = (Boolean) httpSession.getAttribute("registered");
        if(Boolean.TRUE.equals(registered)){
           return "side log";
        }
        return "redirect:/signup";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request){
        return "redirect:/login";
    }

    @GetMapping("/create-apartment")
    public String createApartment(){
        return "add";
    }

    @PreAuthorize("hasAuthority('SCOPE_REFUGEE')")
    @GetMapping("/refugee-settings")
    public String refugeeSettings(HttpSession session, Model model){
        String username = (String)session.getAttribute("username");
        RefugeeDetails refugeeDetails = refugeeRepository.findByUserEntityUsername(username);

        if(refugeeDetails == null){
            return "redirect:/login";
        }

       model.addAttribute("refugeeDetails", refugeeDetails);
        return "newset";
    }


    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    @GetMapping("/host-settings")
    public String hostSettings(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        HostDetails hostDetails = hostRepository.findByUserEntityUsername(username);

        if(hostDetails == null){
            return "redirect:/login";
        }
        String hostName = hostDetails.getFirstName() + " " + hostDetails.getLastName();
        model.addAttribute("hostName", hostName);
        model.addAttribute("hostDetails", hostDetails);
        return "set";
    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    @GetMapping("/create-listing")
    public String createListing(){
        return "add";
    }

    @GetMapping("/listing-details/{id}")
    public String listingDetails(@PathVariable int id, Model model) {
        try {
            List<HouseListingImages> imagesForListing = listingService.getSpecificListingImages(id);
            Optional<HouseListing> houseListing = listingRepository.findById(id);

            if (houseListing.isEmpty()) {
                throw new Exception("Listing not found");
            }

            // Clean up image path (remove /listing-details if included)
            for (HouseListingImages image : imagesForListing) {
                String imagePath = image.getImagePath();
                // Remove the '/listing-details' prefix, if present
                if (imagePath.startsWith("/listing-details")) {
                    image.setImagePath(imagePath.replace("/listing-details", ""));
                }
            }

            model.addAttribute("imagesForListing", imagesForListing);
            model.addAttribute("houseListing", houseListing.get());

            return "details"; // Use the appropriate name for your view template
        } catch (Exception ex) {
            // In case of an error, redirect to the home page
            return "redirect:/home";
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    @GetMapping("/clients")
    public String clients(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        HostDetails hostDetails = hostRepository.findByUserEntityUsername(username);

        if(hostDetails == null){
            return "redirect:/login";
        }

        String hostName = hostDetails.getFirstName() + " " + hostDetails.getLastName();
        model.addAttribute("hostName", hostName);
        model.addAttribute("hostDetails", hostDetails);

        return "cli";
    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    @GetMapping("/properties")
    public String properties(HttpSession session, Model model){
        String username = (String)session.getAttribute("username");
        HostDetails hostDetails = hostRepository.findByUserEntityUsername(username);

        List<HouseListing> houseListings = listingRepository.findByHostDetails(hostDetails);

        String hostName = hostDetails.getFirstName() + " " + hostDetails.getLastName();
        if(houseListings.isEmpty()){
            model.addAttribute("hostName", hostName);
            return "prop";
        }

        model.addAttribute("hostName", hostName);
        model.addAttribute("houseListings", houseListings);
        return "prop";
    }

}
