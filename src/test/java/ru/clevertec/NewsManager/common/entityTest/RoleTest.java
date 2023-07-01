package ru.clevertec.NewsManager.common.entityTest;

import org.junit.jupiter.api.Test;
import ru.clevertec.NewsManager.common.utill.RequestName;
import ru.clevertec.NewsManager.entity.user.Role;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {

    @Test
    void shouldCreateRoleWithAllArgsConstructor() {
        Role role = new Role(1L, RequestName.ADMIN.getValue());
        assertThat(role.getId()).isEqualTo(1L);
        assertThat(role.getName()).isEqualTo(RequestName.ADMIN.getValue());
    }

    @Test
    void shouldCreateRoleWithNoArgsConstructor() {
        Role role = new Role();
        assertThat(role.getId()).isNull();
        assertThat(role.getName()).isNull();
    }

    @Test
    void shouldCreateRoleWithBuilder() {
        Role role = Role.builder()
                .id(2L)
                .name(RequestName.ADMIN.getValue())
                .build();
        assertThat(role.getId()).isEqualTo(2L);
        assertThat(role.getName()).isEqualTo(RequestName.ADMIN.getValue());
    }

    @Test
    void shouldSetRoleFields() {
        Role role = new Role();
        role.setId(3L);
        role.setName(RequestName.ADMIN.getValue());
        assertThat(role.getId()).isEqualTo(3L);
        assertThat(role.getName()).isEqualTo(RequestName.ADMIN.getValue());
    }

    @Test
    void shouldHaveToStringMethod() {
        Role role = Role.builder()
                .id(4L)
                .name(RequestName.ADMIN.getValue())
                .build();
        assertThat(role.toString()).isEqualTo("Role(id=4, name=admin)");
    }

    @Test
    void shouldHaveEqualsAndHashCodeMethod() {
        Role role1 = new Role(5L, RequestName.ADMIN.getValue());
        Role role2 = new Role(5L, RequestName.ADMIN.getValue());
        assertThat(role1).isEqualTo(role2);
        assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
    }
}

