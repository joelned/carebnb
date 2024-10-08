package com.example.demo.Controllers;

import com.example.demo.DTOs.UserDTO;
import com.example.demo.DTOs.RefugeeDTO;
import com.example.demo.DTOs.HostDTO;
import com.example.demo.Models.Refugee;
import com.example.demo.Models.Host;
import com.example.demo.Repositories.HostRepository;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefugeeRepository refugeeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HostRepository hostRepository;

    @Transactional
    @PostMapping("/save-refugee-details")
    @PreAuthorize(value="REFUGEE")
    public void saveRefugeeDetails(@RequestBody RefugeeDTO refugeeDTO){
        userService.saveRefugeeDetails(refugeeDTO);
    }
    @Transactional
    @PostMapping("/save-host-details")
    @PreAuthorize(value = "HOST")
    public void saveHostDetails(@RequestBody HostDTO hostDTO){
        userService.saveHostDetails(hostDTO);
    }

    @Transactional
    @PostMapping("/host-signup")
    @PreAuthorize(value = "HOST")
    public ResponseEntity<Object>hostSignup(@RequestBody UserDTO hostRegisterDTO){
        userService.registerHost(hostRegisterDTO);
        return new ResponseEntity<>("Registration Successful", HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/refugee-signup")
    public ResponseEntity<Object>refugeeSignup(@RequestBody UserDTO refugeeRegisterDTO){
        userService.registerHost(refugeeRegisterDTO);
        return new ResponseEntity<>("Registration Successful", HttpStatus.CREATED);
    }

}