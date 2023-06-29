package ru.clevertec.NewsManager.repository;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.NewsManager.dto.request.UserRequestProtos;
import ru.clevertec.NewsManager.entity.user.User;
//@FeignClient(value = "userManager", url = "http://localhost:8084/users")

@Component
@FeignClient(value = "userManager", url = "${USER_MANAGER_URL}")
public interface UserManagementClient {

    /**
     The UserManagementClient interface is a Feign client that communicates with the
     User Management service to perform user-related operations.
     */

    @GetMapping(value = "/authenticate/{token}")
    User authenticateUser(@PathVariable String token);

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid UserRequestProtos.UserRequestDto userDto);

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable @Valid String name);
}

