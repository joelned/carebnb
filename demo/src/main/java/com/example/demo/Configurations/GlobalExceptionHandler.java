package com.example.demo.Configurations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object>handleBadCredentialsException(BadCredentialsException ex){
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object>handleMethodNotValidException(MethodArgumentNotValidException ex){
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error->{
            errors.append(error.getDefaultMessage()).append("\n");
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
