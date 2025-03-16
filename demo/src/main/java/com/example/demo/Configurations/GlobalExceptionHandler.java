package com.example.demo.Configurations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.naming.AuthenticationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException ex, Model model,
                                                RedirectAttributes redirectAttributes){
       redirectAttributes.addFlashAttribute("error", "Bad Credentials");
       return "redirect:/login";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String requestMethodNotSupported(HttpRequestMethodNotSupportedException ex){
        return "redirect:/login";
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException ex){
        return "redirect:/login";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException ex, Model model){
        return "redirect:/";
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex, Model model){
        model.addAttribute("error", "Username not found");
        return "login";
    }
    @ExceptionHandler(CredentialsExpiredException.class)
    public String handleCredentialsExpiredException(CredentialsExpiredException ex, Model model){
        return "login";
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
