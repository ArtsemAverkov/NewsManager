package ru.clevertec.NewsManager.repository;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.NewsManager.dto.request.UserRequestProtos;
import ru.clevertec.NewsManager.entity.user.User;

/**
 The UserManagementClient interface is a Feign client used for interacting with the User Manager service.
 It provides methods for authentication, user creation, and user deletion.
 */
@Component
@FeignClient(value = "userManager", url = "${USER_MANAGER_URL}")
public interface UserManagementClient {

    /**
     Authenticates a user based on the provided token.
     @param token The authentication token.
     @return The User object if authentication is successful.
     */
    @GetMapping(value = "/authenticate/{token}")
    User authenticateUser(@PathVariable String token);

    /**
     Creates a new user.
     @param userDto The UserRequestDto containing the user information.
     @return The ID of the created user.
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Long create(@RequestBody @Valid UserRequestProtos.UserRequestDto userDto);

    /**
     Deletes a user based on the provided name.
     @param name The name of the user to delete.
     @return true if the user was successfully deleted, false otherwise.
     */
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    boolean delete(@PathVariable @Valid String name);
}

