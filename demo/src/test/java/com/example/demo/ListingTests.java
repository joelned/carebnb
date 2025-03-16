package com.example.demo;

import com.example.demo.Controllers.ListingController;
import com.example.demo.DTOs.RefugeeProjection;
import com.example.demo.Models.ListingRequest;
import com.example.demo.Repositories.*;
import com.example.demo.Services.ListingService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.security.Principal;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ListingController.class)
@Import(SecurityTestConfig.class)
public class ListingTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListingRequestRepository listingRequestRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private ListingRequest listingRequest;

    @MockBean
    private HostRepository hostRepository;

    @MockBean
    private RefugeeRepository refugeeRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ListingRepository listingRepository;

    @MockBean
    private ListingService listingService;

    @MockBean
    private Principal principal;

    @BeforeEach
    void setUp(){
        when(principal.getName()).thenReturn("test-user");
    }


    @Test
    void getAllListingRequestTest() throws Exception{
        //Arrange
        List<RefugeeProjection> projection = new ArrayList<>();
        Principal principal = mock(Principal.class);
        when(listingService.getListingRequests(any(Principal.class))).thenReturn(projection);
        //Act
        ResultActions response = mockMvc.perform(get("/api/v1/requests"));

        //Assert
        response.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test-user")
    void approvalOfRequestTest() throws Exception {
        // Arrange
        UUID listingRequestId = UUID.randomUUID();

        doNothing().when(listingService).approveListing(eq(listingRequestId));
        doNothing().when(listingService).sendMail(any(HttpSession.class), eq(listingRequestId));

        // Act
        ResultActions response = mockMvc.perform(post("/api/v1/approve-request")
                .param("listingRequestId", String.valueOf(listingRequestId))
                .principal(principal));  // Pass the mocked principal

        // Assert
        response.andExpect(status().isOk());
    }
}

