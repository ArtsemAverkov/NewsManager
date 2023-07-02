package ru.clevertec.NewsManager.common.entityTest;

import org.junit.jupiter.api.Test;
import ru.clevertec.NewsManager.common.utill.RequestName;
import ru.clevertec.NewsManager.entity.user.Role;
import ru.clevertec.NewsManager.entity.user.User;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The UserTest class is a test class for the User entity.
 * It tests the constructors, builder, setter methods of the User class.
 */
public class UserTest {

    /**
     * Tests the User class's constructor with all arguments.
     * It verifies that the User is created with the correct values.
     */
    @Test
    void shouldCreateUserWithAllArgsConstructor() {

        User user = new User(1L, "user", "pass", new Role(1L, RequestName.ADMIN.getValue()));

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getPassword()).isEqualTo("pass");
        assertThat(user.getRole()).isEqualTo(new Role(1L, RequestName.ADMIN.getValue()));
    }

    /**
     * Tests the User class's constructor with username, password, and role arguments.
     * It verifies that the User is created with the correct values.
     */
    @Test
    void shouldCreateUserWithUsernamePasswordRoleConstructor() {
        User user = new User("admin", "secret", new Role(1L, RequestName.ADMIN.getValue()));

        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isEqualTo("admin");
        assertThat(user.getPassword()).isEqualTo("secret");
        assertThat(user.getRole()).isEqualTo(new Role(1L, RequestName.ADMIN.getValue()));
    }

    /**
     * Tests the User class's default constructor.
     * It verifies that the User is created with null values.
     */
    @Test
    void shouldCreateUserWithNoArgsConstructor() {
        User user = new User();

        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRole()).isNull();
    }

    /**
     * Tests the User class's builder.
     * It verifies that the User is created with the correct values using the builder pattern.
     */
    @Test
    void shouldCreateUserWithBuilder() {
        User user = User.builder()
                .id(2L)
                .username("guest")
                .password("guest")
                .role(new Role(1L, RequestName.ADMIN.getValue()))
                .build();

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getUsername()).isEqualTo("guest");
        assertThat(user.getPassword()).isEqualTo("guest");
        assertThat(user.getRole()).isEqualTo(new Role(1L, RequestName.ADMIN.getValue()));
    }

    /**
     * Tests the setter methods of the User class.
     * It verifies that the User's fields are set correctly.
     */
    @Test
    void shouldSetUserFields() {
        User user = new User();

        user.setId(3L);
        user.setUsername("test");
        user.setPassword("test");
        user.setRole(new Role(1L, RequestName.ADMIN.getValue()));

        assertThat(user.getId()).isEqualTo(3L);
        assertThat(user.getUsername()).isEqualTo("test");
        assertThat(user.getPassword()).isEqualTo("test");
        assertThat(user.getRole()).isEqualTo(new Role(1L, RequestName.ADMIN.getValue()));
    }
}
