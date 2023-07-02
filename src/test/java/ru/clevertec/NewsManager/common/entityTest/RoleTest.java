package ru.clevertec.NewsManager.common.entityTest;

import org.junit.jupiter.api.Test;
import ru.clevertec.NewsManager.common.utill.RequestName;
import ru.clevertec.NewsManager.entity.user.Role;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The RoleTest class is a test class for the Role entity.
 * It tests the constructors, builder, setter methods, toString, equals, and hashCode methods of the Role class.
 */
public class RoleTest {

    /**
     * Tests the Role class's constructor with all arguments.
     * It verifies that the Role is created with the correct values.
     */
    @Test
    void shouldCreateRoleWithAllArgsConstructor() {
        Role role = new Role(1L, RequestName.ADMIN.getValue());
        assertThat(role.getId()).isEqualTo(1L);
        assertThat(role.getName()).isEqualTo(RequestName.ADMIN.getValue());
    }

    /**
     * Tests the Role class's default constructor.
     * It verifies that the Role is created with null values.
     */
    @Test
    void shouldCreateRoleWithNoArgsConstructor() {
        Role role = new Role();
        assertThat(role.getId()).isNull();
        assertThat(role.getName()).isNull();
    }

    /**
     * Tests the Role class's builder.
     * It verifies that the Role is created with the correct values using the builder pattern.
     */
    @Test
    void shouldCreateRoleWithBuilder() {
        Role role = Role.builder()
                .id(2L)
                .name(RequestName.ADMIN.getValue())
                .build();
        assertThat(role.getId()).isEqualTo(2L);
        assertThat(role.getName()).isEqualTo(RequestName.ADMIN.getValue());
    }

    /**
     * Tests the setter methods of the Role class.
     * It verifies that the Role's fields are set correctly.
     */
    @Test
    void shouldSetRoleFields() {
        Role role = new Role();
        role.setId(3L);
        role.setName(RequestName.ADMIN.getValue());
        assertThat(role.getId()).isEqualTo(3L);
        assertThat(role.getName()).isEqualTo(RequestName.ADMIN.getValue());
    }

    /**
     * Tests the toString method of the Role class.
     * It verifies that the toString method returns the expected string representation of the Role object.
     */
    @Test
    void shouldHaveToStringMethod() {
        Role role = Role.builder()
                .id(4L)
                .name(RequestName.ADMIN.getValue())
                .build();
        assertThat(role.toString()).isEqualTo("Role(id=4, name=admin)");
    }

    /**
     * Tests the equals and hashCode methods of the Role class.
     * It verifies that two Role objects with the same values are considered equal and have the same hashCode.
     */
    @Test
    void shouldHaveEqualsAndHashCodeMethod() {
        Role role1 = new Role(5L, RequestName.ADMIN.getValue());
        Role role2 = new Role(5L, RequestName.ADMIN.getValue());
        assertThat(role1).isEqualTo(role2);
        assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
    }
}

