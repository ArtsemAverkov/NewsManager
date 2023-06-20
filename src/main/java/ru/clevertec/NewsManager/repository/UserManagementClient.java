package ru.clevertec.NewsManager.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.NewsManager.dto.user.User;


@Component
@FeignClient(value = "userManager", url = "http://localhost:8084/users")
public interface UserManagementClient {

    /**
     The UserManagementClient interface is a Feign client that communicates with the
     User Management service to perform user-related operations.
     */

    @GetMapping(value = "/authenticate/{token}")
    User authenticateUser(@PathVariable String token);
}

