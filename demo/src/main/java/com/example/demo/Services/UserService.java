package com.example.demo.Services;

import com.example.demo.Models.*;
import com.example.demo.Repositories.HostRepository;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RefugeeRepository refugeeRepository;
    private final HostRepository hostRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder
            , RoleRepository roleRepository, RefugeeRepository refugeeRepository, HostRepository hostRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.refugeeRepository = refugeeRepository;
        this.hostRepository = hostRepository;
    }

    public void registerUser(String username, String password, String roleName, String firstName,
                             String lastName, String email) {
        UserEntity userEntityToBeAuthenticated = userRepository.findByUsername(username);
        if (userEntityToBeAuthenticated != null) {
            logger.warn("Registration Failed: Username [{}] already exists", username);
            throw new IllegalArgumentException("Username Already Exists");
        }
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            logger.warn("Invalid role [{}] during registration attempt", roleName);
            throw new IllegalArgumentException("Role not found");
        }
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        if(roleName.equals("host")){
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setRole(roleList);
            userRepository.save(userEntity);

            HostDetails hostDetails = new HostDetails();
            hostDetails.setFirstName(firstName);
            hostDetails.setLastName(lastName);
            hostDetails.setEmail(email);
            hostDetails.setUserEntity(userEntity);
            hostRepository.save(hostDetails);
        }
        else {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setRole(roleList);
            userRepository.save(userEntity);
        }

        logger.info("User [{}] successfully registered with role [{}]", username, roleName);
    }

    public void registerRefugee(HttpSession httpSession, String name, MultipartFile profileImage,
                                int age, String email, String gender,
                                int dependents, String countryOfFlee,
                                String countryOfOrigin, List<String> languagesSpoken,
                                String preferredLocation, String selfDescription,
                                List<String> specialNeeds) throws IOException, ClassNotFoundException, Exception {
        String username = (String) httpSession.getAttribute("username");
        UserEntity refugee = userRepository.findByUsername(username);
        if (refugee == null) {
            throw new Exception("Refugee Not Found");
        }
        try {
            RefugeeDetails refugeeDetails = new RefugeeDetails();
            refugeeDetails.setName(name);
            refugeeDetails.setAge(age);
            refugeeDetails.setEmail(email);
            refugeeDetails.setGender(gender);
            refugeeDetails.setDependents(dependents);
            refugeeDetails.setUserEntity(refugee);
            refugeeDetails.setCountryOfFlee(countryOfFlee);
            refugeeDetails.setUserEntity(refugee);
            refugeeDetails.setCountryOfOrigin(countryOfOrigin);
            refugeeDetails.setLanguagesSpoken(languagesSpoken);
            refugeeDetails.setPreferredLocation(preferredLocation);
            refugeeDetails.setSelfDescription(selfDescription);
            refugeeDetails.setSpecialNeeds(specialNeeds);
            refugeeRepository.save(refugeeDetails);
            File imagePath = new File("Pictures/");
            Path path = imagePath.toPath();

            if(!imagePath.exists()){
                Files.createDirectories(path);
                System.out.println("Folder created at " + path.toAbsolutePath());
            }
            saveImageToFile(profileImage, imagePath, refugeeDetails);
            System.out.println("Folder created at " + imagePath);
        }
        catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }

    public void saveImageToFile(MultipartFile image, File pathDirectory, RefugeeDetails refugee) throws Exception{
        try{
            String fileExtension = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + fileExtension;
            String pathToImage = pathDirectory.toString() + "\\" + fileName;
            System.out.println(pathDirectory.toString());
            Path path = Paths.get(pathToImage);
            Files.write(path, image.getBytes());
            RefugeeDetails refugeeDetails = refugeeRepository.findByName(refugee.getName());
            String savedImagePath = pathToImage.replace("\\", "/");
            refugeeDetails.setProfilePicturePath(savedImagePath);
            refugeeRepository.save(refugeeDetails);
        }
        catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }

}
