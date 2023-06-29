package ru.clevertec.NewsManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.NewsManager.dto.request.UserRequestProtos;
import ru.clevertec.NewsManager.repository.UserManagementClient;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
   private final UserManagementClient userManagementClient;

    /**
     * Creates a new user.
     * @param userDto the UserRequestDto containing the user details
     * @return the ID of the created user
     */
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid UserRequestProtos.UserRequestDto userDto){
        return userManagementClient.create(userDto);
    }

    /**
     * Deletes a user by their name.
     * @param name the name of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable @Valid String name){
        return  userManagementClient.delete(name);
    }
}
