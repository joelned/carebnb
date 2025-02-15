package com.example.demo.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {

    @GetMapping("/login")
    public String index(){
        return "index";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }
    @GetMapping("/login?error=true")
    public String error2(){
        return "login";
    }

    @GetMapping("/access-denied")
    public String error(){
        return "accessdenied";
    }

    @GetMapping("/host")
    public ResponseEntity<String> host(){
        return new ResponseEntity<>("This is a host page", HttpStatus.OK);
    }


    @GetMapping("/refugee")
    public ResponseEntity<String> refugee(){
       return new ResponseEntity<>("This is a refugee page", HttpStatus.OK);
    }
}
