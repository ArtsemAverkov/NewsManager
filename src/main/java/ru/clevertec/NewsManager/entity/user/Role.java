package ru.clevertec.NewsManager.entity.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @NotNull(message = "ID cannot be empty")
    private Long id;

    @NotNull(message = "Role name cannot be empty")
    private String name;
}
