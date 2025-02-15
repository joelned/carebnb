package com.example.demo.Services;

import com.example.demo.DTOs.RefugeeDTO;
import com.example.demo.Models.RefugeeDetails;
import com.example.demo.Models.Role;
import com.example.demo.Models.UserEntity;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RefugeeRepository refugeeRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder
            , RoleRepository roleRepository, RefugeeRepository refugeeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.refugeeRepository = refugeeRepository;
    }

    public void registerUser(String username, String password, String roleName) {
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
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setRole(Collections.singletonList(role));

        userRepository.save(userEntity);
        logger.info("User [{}] successfully registered with role [{}]", username, roleName);
    }

    @PreAuthorize("hasAuthority('SCOPE_REFUGEE')")
    public void registerRefugee(Principal principal, RefugeeDTO refugeeDTO) throws IOException, ClassNotFoundException {
        UserEntity refugee = userRepository.findByUsername(principal.getName());
        RefugeeDetails refugeeDetails = new RefugeeDetails();
        refugeeDetails.setName(refugeeDTO.getName());
        refugeeDetails.setAge(refugeeDTO.getAge());
        refugeeDetails.setEmail(refugeeDTO.getEmail());
        refugeeDetails.setGender(refugeeDTO.getGender());
        refugeeDetails.setDependents(refugeeDTO.getDependents());
        refugeeDetails.setUserEntity(refugee);
        refugeeDetails.setCountryOfFlee(refugeeDTO.getCountryOfFlee());
        refugeeDetails.setCountryOfOrigin(refugeeDTO.getCountryOfOrigin());
        refugeeDetails.setLanguagesSpoken(refugeeDTO.getLanguagesSpoken());
        refugeeDetails.setPreferredLocation(refugeeDTO.getPreferredLocation());
        refugeeDetails.setSelfDescription(refugeeDTO.getSelfDescription());
        refugeeDetails.setSpecialNeeds(refugeeDTO.getSpecialNeeds());
        refugeeRepository.save(refugeeDetails);
    }

}
