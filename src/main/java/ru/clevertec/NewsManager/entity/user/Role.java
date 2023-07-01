package ru.clevertec.NewsManager.entity.user;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private Long id;
    private String name;
}
