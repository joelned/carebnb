package com.example.demo.Controllers;

import com.example.demo.DTOs.ApprovalResponse;
import com.example.demo.DTOs.ErrorResponse;
import com.example.demo.DTOs.RefugeeDTO;
import com.example.demo.DTOs.RegisterDTO;
import com.example.demo.Models.Role;
import com.example.demo.Models.UserEntity;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.TokenService;
import com.example.demo.Services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefugeeRepository refugeeRepository;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/{role}/register")
    @Transactional
    public ResponseEntity<Object> register(@PathVariable String role, @Valid @RequestBody RegisterDTO registerDTO) {
        try {
            userService.registerUser(registerDTO.getUsername(), registerDTO.getPassword(), role);
            return ResponseEntity.ok("Registration Successful");
        } catch (IllegalArgumentException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenService.generateToken(authentication);
            UserEntity user = userRepository.findByUsername(username);
            List<Role> role = user.getRole();
            List<String> nameOfRole = role.stream().map(Role::getName).toList();
            ResponseCookie responseCookie = ResponseCookie.from("jwt", token)
                    .secure(true)
                    .path("/")
                    .httpOnly(true)
                    .maxAge(3600)
                    .sameSite("None")
                    .build();

            logger.info("Login Successful and Performed.");
            ApprovalResponse response = new ApprovalResponse("Login Successful",
                    true, LocalDateTime.now());
            if(nameOfRole.contains("HOST")){
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                        .location(URI.create("/host"))
                        .body(response);
            }else {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                        .location(URI.create("/refugee"))
                        .body(response);
            }
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
            logger.error("Login from [{}] failed: {}", username, ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .location(URI.create("/login?error=true"))
                    .body(errorResponse);
        }

    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Object> registerRefugee(Principal principal, @RequestBody RefugeeDTO refugeeDTO) {
        try {
            ApprovalResponse approvalResponse = new ApprovalResponse("Refugee Details Registered Successfully",
                    true, LocalDateTime.now());
            userService.registerRefugee(principal, refugeeDTO);
            return ResponseEntity.ok(approvalResponse);
        }
        catch(Exception ex){
            ErrorResponse errorResponse = new ErrorResponse("Conflict", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String>test(){
        return new ResponseEntity<>("Test Successful", HttpStatus.OK);
    }
}