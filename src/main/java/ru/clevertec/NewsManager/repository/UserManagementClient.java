package ru.clevertec.NewsManager.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.NewsManager.dto.response.User;


@Component
@FeignClient(value = "userManager", url = "http://localhost:8084/users")
public interface UserManagementClient {

    @GetMapping(value = "/authenticate/{token}")
    public User authenticateUser(@PathVariable String token);
}

