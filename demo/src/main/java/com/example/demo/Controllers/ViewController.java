package com.example.demo.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ViewController {
    @GetMapping
    public String helloWord(){
        return "Hello World";
    }

    @GetMapping("/authorized")
    public String authorized(){
        return "This is authorized";
    }
}
