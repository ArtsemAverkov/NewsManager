package ru.clevertec.NewsManager.common.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.NewsManager.NewsManagerApplication;
import ru.clevertec.NewsManager.common.WireMockInitializer;
import ru.clevertec.NewsManager.common.utill.RequestId;
import ru.clevertec.NewsManager.common.utill.RequestName;
import ru.clevertec.NewsManager.controller.NewsController;
import ru.clevertec.NewsManager.controller.UserController;
import ru.clevertec.NewsManager.dto.request.NewsRequestProtos;
import ru.clevertec.NewsManager.dto.request.UserRequestProtos;
import ru.clevertec.NewsManager.repository.UserManagementClient;
import ru.clevertec.NewsManager.security.JwtTokenGenerator;
import ru.clevertec.NewsManager.security.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.NewsManager.common.utill.CommentBuilder.createUserDetails;

@ContextConfiguration(classes = NewsManagerApplication.class)
@WebMvcTest({UserController.class, SecurityConfig.class, JwtTokenGenerator.class})
@AutoConfigureMockMvc
@DisplayName("Testing News Controller")
public class UserControllerTest {

    @MockBean
    private UserManagementClient userManagementClient;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setup() {
        WireMockInitializer.setup();
    }

    @AfterAll
    static void teardown() {
        WireMockInitializer.teardown();
    }

    @Test
    public void create() throws Exception {
        when(userManagementClient.create(any(UserRequestProtos.UserRequestDto.class)))
                .thenReturn(RequestId.VALUE_1.getValue());
        String s = "{\n" +
                "  \"username\": \"user\",\n" +
                "  \"password\": \"password\",\n" +
                "  \"role\": \"SUBSCRIBER\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .with(user(createUserDetails()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(RequestId.VALUE_1.getValue())));
        verify(userManagementClient).create(any(UserRequestProtos.UserRequestDto.class));
        SecurityContextHolder.clearContext();
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{name}", RequestName.USERNAME.getValue())
                        .with(user(createUserDetails())))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(userManagementClient).delete(String.valueOf(RequestName.USERNAME.getValue()));
        SecurityContextHolder.clearContext();
    }
}
