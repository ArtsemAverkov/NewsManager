package ru.clevertec.NewsManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.NewsManager.dto.request.UserRequestProtos;
import ru.clevertec.NewsManager.repository.UserManagementClient;
import ru.clevertec.controllerlogspringbootstarter.aop.loger.IncludeLog;
import ru.clevertec.exceptionhandlerspringbootstarter.EnableExceptionHandling;


@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@EnableExceptionHandling
@IncludeLog
public class UserController {
   private final UserManagementClient userManagementClient;

    /**
     * Creates a new user.
     * @param userDto the UserRequestDto containing the user details
     * @return the ID of the created user
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
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
