package com.example.demo.Services;

import com.example.demo.DTOs.ListingImagesDTO;
import com.example.demo.DTOs.RefugeeProjection;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import jakarta.servlet.http.HttpSession;
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
import java.time.LocalDate;
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

    public void uploadListing(HttpSession session, String name, int maxGuests, String description, String address,
                              List<String> offerings, MultipartFile[] images) throws Exception {

        String username = (String)session.getAttribute("username");
        System.out.println(username);
        HostDetails hostDetails = hostRepository.findByUserEntityUsername(username);
        System.out.println("Recieved "+ images.length + "images");
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
            File imagePath = new File("Pictures/");
            Path path = imagePath.toPath();
            if (!imagePath.exists()) {
                Files.createDirectories(path);
            }
            for (MultipartFile image : images) {
                HouseListingImages listingImages = saveImageToFile(image, imagePath, houseListing);
                if(listingImages != null){
                    houseListing.getListingImages().add(listingImages);
                }
            }
            listingRepository.save(houseListing);
        } catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }

   public HouseListingImages saveImageToFile(MultipartFile image, File pathDirectory, HouseListing houseListing) throws Exception{
        try{
        String fileExtension = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        String pathToImage = pathDirectory.toString() + "\\" + fileName;
        Path path = Paths.get(pathToImage);
        Files.write(path, image.getBytes());
        HouseListingImages houseListingImages = new HouseListingImages();
        String savedImagePath = pathToImage.replace("\\", "/");
        houseListingImages.setImagePath(savedImagePath);
        houseListingImages.setHouseListing(houseListing);
        imagesRepository.save(houseListingImages);
        return houseListingImages;
        }
        catch(Exception ex){
            logger.error(ex.getMessage());
            return null;
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_REFUGEE')")
    public void applyForListings(String name, LocalDate checkIn, LocalDate checkOut, HttpSession session) throws InstanceNotFoundException {
        String username = (String) session.getAttribute("username");
        HouseListing houseListing = listingRepository.findByName(name);
        RefugeeDetails refugeeDetails = refugeeRepository.findByName(username);
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
        request.setHouseListing(houseListing);
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        request.setApproved(false);
        request.setHostDetails(houseListing.getHostDetails());
        listingRequestRepository.save(request);

    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    public List<RefugeeProjection> getListingRequests(Principal principal){
        HostDetails host = hostRepository.findByUserEntityUsername(principal.getName());
        return listingRequestRepository.findRefugeeDetailsByHostDetails(host);
    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    public void approveListing(int listingRequestId){
        listingRequestRepository.approveListingRequest(listingRequestId);
    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    public void declineRequest(int listingRequestId){
        listingRequestRepository.deleteById(listingRequestId);
    }

    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    public void sendDeclineMail(HttpSession session, int listingRequestId, String refugeeName) {
        try {

            String message = "Dear " + refugeeName + ",\n\n" +
                    "We are regret to inform you that your request to stay with a host has been declined.\n\n" +
                    "You may choose to apply for another listing. We wish you the best in your endeavors. \n\n" +
                    "Kind regards,\n" +
                    "The Refugee Support Team";

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("Request Declined From Host");
            simpleMailMessage.setTo("ekweghjoel4@gmail.com");
            simpleMailMessage.setFrom("joelekwegh3@gmail.com");
            simpleMailMessage.setText(message);
            mailSender.send(simpleMailMessage);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            logger.error(String.valueOf(ex));
        }
    }



    @PreAuthorize("hasAuthority('SCOPE_HOST')")
    public void sendMail(HttpSession session, int listingRequestId) {
        try {
            String username = (String) session.getAttribute("username");
            HostDetails hostDetails = hostRepository.findByUserEntityUsername(username);
            ListingRequest request = listingRequestRepository.findByListingRequestId(listingRequestId);
            HouseListing listing = request.getHouseListing();
            RefugeeDetails refugeeDetails = request.getRefugeeDetails();
            //listing is going to be null unless request  is filled successfully
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
            System.out.println(ex.getMessage());
            logger.error(String.valueOf(ex));
        }
    }



    public List<ListingImagesDTO>getListingImages(){
        return imagesRepository.findListingImages();
    }
    public List<HouseListingImages>getSpecificListingImages(int id){
        return imagesRepository.findByHouseListing_HouseListingId(id);
    }
}