package com.example.demo.Services;

import com.example.demo.DTOs.HostDTO;
import com.example.demo.DTOs.RefugeeDTO;
import com.example.demo.DTOs.UserDTO;
import com.example.demo.Models.Host;
import com.example.demo.Models.Refugee;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.HostRepository;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefugeeRepository refugeeRepository;

    public void registerHost(UserDTO userDTO){
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
        if(user.isEmpty()){
            throw new IllegalArgumentException("Username is already taken");
        }
        User hostUser = new User();
        hostUser.setUsername(userDTO.getUsername());
        hostUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Role role = roleRepository.findByName("USER");
        hostUser.setRoles(Collections.singletonList(role));

        userRepository.save(hostUser);
    }
    public void registerRefugee(UserDTO userDTO){
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
        if(user.isEmpty()){
            throw new IllegalArgumentException("Username is already taken");
        }
        User refugee = new User();
        refugee.setUsername(userDTO.getUsername());
        refugee.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Role role = roleRepository.findByName("USER");
        refugee.setRoles(Collections.singletonList(role));

        userRepository.save(refugee);
    }

    public void saveHostDetails(HostDTO hostDTO){
        Host registeredHost = hostRepository.findByEmail(hostDTO.getEmail());
        if(registeredHost == null){
            new ResponseEntity<>("Account Exists", HttpStatus.OK);
            return;
        }
        registeredHost.setFirstName(hostDTO.getFirstName());
        registeredHost.setLastName(hostDTO.getLastName());
        registeredHost.setPhoneNumber(hostDTO.getPhoneNumber());
        registeredHost.setEnabled(false);
        registeredHost.setRole("HOST");

        hostRepository.save(registeredHost);
        new ResponseEntity<>("User Registered Successfully", HttpStatus.OK);
    }

    public void saveRefugeeDetails(RefugeeDTO refugeeDTO){
        Refugee refugee = refugeeRepository.findByEmail(refugeeDTO.getEmail());
        if(refugee == null){
            new ResponseEntity<>("Account Exists", HttpStatus.CONFLICT);
            return;
        }
        Refugee registeredRefugee= new Refugee();
        registeredRefugee.setFirstName(refugeeDTO.getFirstName());
        registeredRefugee.setLastName(refugeeDTO.getLastName());
        registeredRefugee.setAge(refugeeDTO.getAge());
        registeredRefugee.setCountry(refugeeDTO.getCountry());
        registeredRefugee.setRole("REFUGEE");
        registeredRefugee.setPhoneNumber(refugeeDTO.getPhoneNumber());
        registeredRefugee.setEnabled(false);
        registeredRefugee.setBookings(refugeeDTO.getBookings());

        refugeeRepository.save(registeredRefugee);
        new ResponseEntity<>("User Registered Successfully", HttpStatus.OK);

    }
}
