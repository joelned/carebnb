package com.example.demo.Services;

import com.example.demo.DTOs.RefugeeProjection;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.management.InstanceNotFoundException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ListingService {
    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private ListingImagesRepository imagesRepository;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ListingRequestRepository listingRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefugeeRepository refugeeRepository;
    Logger logger = LoggerFactory.getLogger(ListingService.class);

    public void uploadListing(String name, int maxGuests, String description, String address,
                              List<String> offerings, List<MultipartFile> images, Principal principal) throws Exception {

        String username = principal.getName();
        HostDetails hostDetails = hostRepository.findByUserEntityUsername(username);
        if(hostDetails == null){
            throw new Exception("Host Details not Found for user: " + username);
        }
        HouseListing houseListing = new HouseListing();
        try {
            houseListing.setDescription(description);
            houseListing.setAddress(address);
            houseListing.setName(name);
            houseListing.setMaxGuests(maxGuests);
            houseListing.setOfferings(offerings);
            houseListing.setHostDetails(hostDetails);

            listingRepository.save(houseListing);
            logger.info("Listing Details Saved");
            File imagePath = new File("/Pictures/carebnb/");
            Path path = imagePath.toPath();
            if (!imagePath.exists()) {
                Files.createDirectories(path);
                System.out.println("Folder created at " + path.toAbsolutePath());
            }
            for (MultipartFile image : images) {
                saveImageToFile(image, imagePath, houseListing);
            }
        } catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }

   public void saveImageToFile(MultipartFile image, File pathDirectory, HouseListing houseListing) throws Exception{
        try{
        String fileExtension = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        String pathToImage = pathDirectory.toString() + fileName;
        Path path = Paths.get(pathToImage);
        Files.write(path, image.getBytes());
        HouseListingImages houseListingImages = new HouseListingImages();
        houseListingImages.setImagePath(pathToImage);
        houseListingImages.setHouseListing(houseListing);
        imagesRepository.save(houseListingImages);
        }
        catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_REFUGEE')")
    public void applyForListings(String name, Principal principal) throws InstanceNotFoundException {
        HouseListing houseListing = listingRepository.findByName(name);
        RefugeeDetails refugeeDetails = refugeeRepository.findByName(principal.getName());
        System.out.println(principal.getName());
        if(houseListing == null){
           throw new InstanceNotFoundException("House Listing not found");
        }
       ListingRequest previousRequest = listingRequestRepository.findFirstByRefugeeDetailsAndApprovedTrue(refugeeDetails);
        if(previousRequest != null){
            logger.info("Refugee has applied for more than one listing");
            return;
        }
        ListingRequest request = new ListingRequest();
        request.setRefugeeDetails(refugeeDetails);
        listingRequestRepository.save(request);

    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    public List<RefugeeProjection> getListingRequests(Principal principal){
        HostDetails host = hostRepository.findByUserEntityUsername(principal.getName());
        return listingRequestRepository.findRefugeeDetailsByHostDetails(host);
    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    public void approveListing(Principal principal, int listingRequestId){
        listingRequestRepository.approveListingRequest(listingRequestId);
    }


    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    public void sendMail(Principal principal, int listingRequestId) {
        try {
            HostDetails hostDetails = hostRepository.findByUserEntityUsername(principal.getName());
            ListingRequest request = listingRequestRepository.findByListingRequestId(listingRequestId);
            HouseListing listing = request.getHouseListing();
            RefugeeDetails refugeeDetails = request.getRefugeeDetails();
            String message = "Dear " + refugeeDetails.getName() + ",\n\n" +
                    "We are pleased to inform you that your request to stay with a host has been accepted.\n\n" +
                    "You will be staying with " + hostDetails.getFirstName() + " " + hostDetails.getLastName() +
                    " at the following address:\n" +
                    listing.getAddress()+ "\n\n" +
                    "Please make sure to contact your host at "+ hostDetails.getEmail() + " " +
                    "ahead of your arrival to confirm any final details.\n\n" +
                    "We wish you all the best during your stay and are here to support you in any way we can.\n\n" +
                    "Kind regards,\n" +
                    "The Refugee Support Team";
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("Request Accepted From Host");
            simpleMailMessage.setTo("ekweghjoel4@gmail.com");
            simpleMailMessage.setFrom("joelekwegh3@gmail.com");
            simpleMailMessage.setText(message);
            mailSender.send(simpleMailMessage);
        } catch(Exception ex){
            System.out.println(ex);
        }
    }
}