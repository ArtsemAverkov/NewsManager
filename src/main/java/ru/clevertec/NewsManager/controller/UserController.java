package ru.clevertec.NewsManager.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.NewsManager.dto.user.UserDto;
import ru.clevertec.NewsManager.repository.UserManagementClient;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementClient userManagementClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid UserDto userDto){
        return  userManagementClient.create(userDto);
    }

    @PostMapping(value = "/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createJournalist(@RequestBody @Valid UserDto userDto){
        return  userManagementClient.create(userDto);
    }

}
