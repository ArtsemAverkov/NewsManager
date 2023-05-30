package ru.clevertec.NewsManager.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.NewsManager.dto.response.User;
import ru.clevertec.NewsManager.repository.UserManagementClient;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementClient userManagementClient;

    @GetMapping(value= "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User readUser(@PathVariable @Valid String name) {
        return userManagementClient.read(name);
    }
}
