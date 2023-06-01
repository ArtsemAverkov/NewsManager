package ru.clevertec.NewsManager.controller;


import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping(value = "/welcome", produces = MediaType.APPLICATION_JSON_VALUE)
    public String loginSuccess(Authentication authentication) {
        String username = authentication.getName();
        return "Successful login! Welcome, " + username + "!";
    }

    @GetMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE )
    public String handleLoginError() {
        return "Login failed";
    }
}