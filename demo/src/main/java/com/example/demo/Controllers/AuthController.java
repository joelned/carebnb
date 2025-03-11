package com.example.demo.Controllers;

import com.example.demo.Configurations.SecurityConfig;
import com.example.demo.Models.RefugeeDetails;
import com.example.demo.Models.Role;
import com.example.demo.Models.UserEntity;
import com.example.demo.Repositories.ListingRepository;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.TokenService;
import com.example.demo.Services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDateTime;
import java.util.List;

@Controller
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

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private TemplateEngine templateEngine;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/{role}/register")
    @Transactional
    public String register(@PathVariable String role,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false)String firstName,
                           @RequestParam(required = false) String lastName, Model model, HttpSession httpSession,
                            RedirectAttributes redirectAttribute
    ) {
        try {
            userService.registerUser(username, password,role,firstName, lastName, email);
            if(role.equals("host")){
                redirectAttribute.addFlashAttribute("success", "Registration Successful");
                httpSession.setAttribute("username", username);
                httpSession.setAttribute("registered", true);
                logger.info("{} registered as HOST", username);
                return "redirect:/signup/host";
            }
            httpSession.setAttribute("username",username);
            httpSession.setAttribute("registered", true);
            return "redirect:/get-started";

        } catch (IllegalArgumentException ex) {
            if(role.equals("host")){
                model.addAttribute("error", "Username already exists");
                return "sign";
            } else {
                model.addAttribute("error", "Username already exists");
                return "refugee-signup";
            }
        }

    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                                      Model model, HttpServletResponse response,
                                      RedirectAttributes
                        redirectAttributes, HttpSession session) {
        UserEntity user = userRepository.findByUsername(username);
        List<Role> role = user.getRole();
        List<String> nameOfRole = role.stream().map(Role::getName).toList();
        if(nameOfRole.contains("REFUGEE")){
            session.setAttribute("role", "REFUGEE");
           RefugeeDetails details = refugeeRepository.findByUserEntity(user);
           if(details == null){
               redirectAttributes.addFlashAttribute("fillRefugeeDetails",
                       "Please fill refugee details");
               return "redirect:/login";
           }
        }
        if(nameOfRole.contains("HOST")){
            session.setAttribute("role", "HOST");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("username", username);
            String token = tokenService.generateToken(authentication);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(3600);

            response.addCookie(cookie);
            logger.info("{} logged in at " + LocalDateTime.now() + "as {} ", username, nameOfRole);

            return "redirect:/home";
    }

    @PostMapping("/register")
    public String registerRefugee(HttpSession httpSession, @RequestParam String name,
                                  @RequestParam MultipartFile profileImage,
                                  @RequestParam int age, @RequestParam String email, @RequestParam String gender,
                                  @RequestParam int dependents, @RequestParam String countryOfFlee,
                                  @RequestParam String countryOfOrigin, @RequestParam
                                      List<String> languagesSpoken,
                                  @RequestParam String preferredLocation, @RequestParam String selfDescription,
                                  @RequestParam List<String> specialNeeds, Model model) {

        try {
            userService.registerRefugee(httpSession, name, profileImage, age, email, gender, dependents
            ,countryOfFlee, countryOfOrigin, languagesSpoken, preferredLocation, selfDescription,
                    specialNeeds);
            model.addAttribute("message", "Details Saved Successfully");
            logger.info("{} registered as REFUGEE", name + " at " + LocalDateTime.now());
            return "redirect:/login";
        }
        catch(Exception ex){
            model.addAttribute("error", ex.getMessage());
            logger.error(ex.toString());
            return "side log";
        }
    }

   @GetMapping("/test")
   public String test(Model model){
       model.addAttribute("message", "model working");
       return "test";
   }
}