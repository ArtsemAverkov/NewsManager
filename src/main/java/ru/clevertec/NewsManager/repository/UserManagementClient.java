package ru.clevertec.NewsManager.repository;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.NewsManager.dto.response.User;
import ru.clevertec.NewsManager.dto.user.UserDto;

@Component
@FeignClient(value = "userManager", url = "http://localhost:8090/users")
public interface UserManagementClient {

    @GetMapping(value = "{name}")
    @ResponseStatus(HttpStatus.OK)
    User read(@PathVariable @Valid String name);


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid UserDto userDto);
    }


