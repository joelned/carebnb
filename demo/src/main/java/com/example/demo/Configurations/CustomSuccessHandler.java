package com.example.demo.Configurations;

import com.example.demo.Models.HouseListing;
import com.example.demo.Repositories.ListingRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ListingRepository listingRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        List<HouseListing> listing = listingRepository.findAll();

        response.sendRedirect("/home");
    }
}
