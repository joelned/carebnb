package com.example.demo;

import com.example.demo.Controllers.AuthController;
import com.example.demo.DTOs.RefugeeDTO;
import com.example.demo.DTOs.RegisterDTO;
import com.example.demo.Models.Role;
import com.example.demo.Models.UserEntity;
import com.example.demo.Repositories.RefugeeRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.TokenService;
import com.example.demo.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import(SecurityTestConfig.class)
public class AuthenticationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RefugeeRepository refugeeRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserEntity userEntity;
    private Role role;
    private RegisterDTO registerDTO;
    private RefugeeDTO refugeeDTO;
    private Principal principal;
    private Authentication authentication;

    @BeforeEach
    void setup(){
        List<Role> role = new ArrayList<>();
        Role refugeeRole = new Role("REFUGEE");
        Role hostRole = new Role("HOST");
        role.add(refugeeRole);
        role.add(hostRole);

        registerDTO = new RegisterDTO();
        registerDTO.setUsername("Ayo");
        registerDTO.setPassword("Ragnarok");
        registerDTO.setRole(role);


        refugeeDTO = new RefugeeDTO();
        refugeeDTO.setName("Ayo");
        refugeeDTO.setAge(23);
        refugeeDTO.setEmail("ayo@gmail.com");
        refugeeDTO.setDependents(4);
        refugeeDTO.setCountryOfFlee("Afghanistan");
        refugeeDTO.setGender("Male");
        List<String>specialNeeds = new ArrayList<>();
        specialNeeds.add("Wheel Chair");
        specialNeeds.add("Comfortable Bed");
        refugeeDTO.setSpecialNeeds(specialNeeds);
        refugeeDTO.setPreferredLocation("Lagos");
        refugeeDTO.setCountryOfOrigin("Nigeria");
        refugeeDTO.setUserEntity(userEntity);
        refugeeDTO.setLanguagesSpoken(Collections.singletonList("Spanish"));
        refugeeDTO.setSelfDescription("Hard working individual seeking a better life");

        userEntity.setUsername("test-user");
        userEntity.setPassword("password");
        userEntity.setRole(Collections.singletonList(role.get(1)));
        authentication = mock(Authentication.class);
    }

    @Test
    void registrationSuccessful() throws Exception{
        //Arrange
        doNothing().when(userService).registerUser(anyString(), anyString(), anyString());

        //Act
        ResultActions response = mockMvc.perform(post("/api/v1/auth/refugee/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));

        //Assert
        response.andExpect(status().isOk())
                .andExpect(content().string("Registration Successful"));

        verify(userService, times(1)).registerUser(registerDTO.getUsername(),
                registerDTO.getPassword(), "refugee");
    }

    @Test
    void registrationConflict() throws Exception{
        //Arrange
        doThrow(new IllegalArgumentException("Username Already Exists")).when(userService).registerUser(
                anyString(), anyString(), anyString()
        );

        //Act
        ResultActions response = mockMvc.perform(post("/api/v1/auth/refugee/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));

        response.andExpect(status().isConflict())
                .andExpect(content().string(containsString("Username Already Exists")));

        //Assert
        verify(userService, times(1)).registerUser(registerDTO.getUsername(),
                registerDTO.getPassword(), "refugee");
    }

    @Test
    void loginSuccessRefugee()throws Exception{
       //Arrange
       when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
               .thenReturn(authentication);

       when(tokenService.generateToken(authentication)).thenReturn("dummy-jwt-token");
       when(userRepository.findByUsername("roland")).thenReturn(userEntity);

       //Act
        ResultActions response = mockMvc.perform(post("/api/v1/auth/login")
                .param("username", "roland")
                .param("password", "ragnarok")
        );

        response.andExpect(status().isFound())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(content().string(containsString("Login Successful")));

    }

    @Test
    void loginSuccessfulHost() throws Exception{
        List<Role> role = new ArrayList<>();
        Role hostRole = new Role("HOST");
        role.add(hostRole);

        userEntity.setRole(role);
       when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(
               authentication
       );
       when(tokenService.generateToken(authentication)).thenReturn("dummy-jwt-token");
       when(userRepository.findByUsername("test-user")).thenReturn(userEntity);

       ResultActions response = mockMvc.perform(post("/api/v1/auth/login")
               .param("username", "test-user")
               .param("password", "password")
       );

       response.andExpect(status().isFound())
               .andExpect(header().exists("Set-Cookie"))
               .andExpect(content().string(containsString("Login Successful")));
    }

    @Test
    void loginUnauthorized() throws Exception{
        //Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).
                thenThrow(new RuntimeException("Invalid Credentials"));

        //Act
        ResultActions response = mockMvc.perform(post("/api/v1/auth/login")
                .param("username", "john-doe")
                .param("password", "password"));

        //Assert
        response.andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid Credentials")));

    }
}