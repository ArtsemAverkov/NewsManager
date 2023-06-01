package ru.clevertec.NewsManager.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String password;
    private RoleDto roles;
}
