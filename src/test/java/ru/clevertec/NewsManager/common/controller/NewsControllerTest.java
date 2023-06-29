package ru.clevertec.NewsManager.common.controller;

import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.NewsManager.NewsManagerApplication;
import ru.clevertec.NewsManager.common.WireMockInitializer;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverCommentsRequestDto;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverNewsRequestDto;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverNewsResponseDto;
import ru.clevertec.NewsManager.common.utill.RequestId;
import ru.clevertec.NewsManager.common.utill.RequestName;
import ru.clevertec.NewsManager.controller.NewsController;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.response.NewsResponseDto;
import ru.clevertec.NewsManager.security.JwtTokenGenerator;
import ru.clevertec.NewsManager.security.SecurityConfig;
import ru.clevertec.NewsManager.service.news.NewsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = NewsManagerApplication.class)
@WebMvcTest({NewsController.class, SecurityConfig.class, JwtTokenGenerator.class})
@ExtendWith({ValidParameterResolverNewsRequestDto.class,
             ValidParameterResolverCommentsRequestDto.class,
             ValidParameterResolverNewsResponseDto.class})
@AutoConfigureMockMvc
@DisplayName("Testing News Controller")
public class NewsControllerTest {

    @MockBean
    private NewsService newsServiceService;

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

    /**
     * Test for creating a news.
     * @param newsRequestDto the news request DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void create(NewsRequestDto newsRequestDto) throws Exception {
        when(newsServiceService.create(any(NewsRequestDto.class))).thenReturn(RequestId.VALUE_1.getValue());
        mockMvc.perform(MockMvcRequestBuilders.post("/news")
                        .with(user(createUserDetails()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(newsRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(RequestId.VALUE_1.getValue())));
        verify(newsServiceService).create(any(NewsRequestDto.class));
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for updating a news.
     * @param newsRequestDto the news request DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void update(NewsRequestDto newsRequestDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/news/{id}", RequestId.VALUE_1.getValue())
                        .with(user(createUserDetails()))
                .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(newsRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(newsServiceService).update(any(NewsRequestDto.class), any());
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for deleting a news.
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", RequestId.VALUE_1.getValue())
                        .with(user(createUserDetails())))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(newsServiceService).delete(RequestId.VALUE_1.getValue());
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for reading a news with comments.
     * @param newsResponseDto the news response DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void readWithComments(NewsResponseDto newsResponseDto) throws Exception {
        when(newsServiceService.readNewsWithComments(RequestId.VALUE_1.getValue())).thenReturn(newsResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/news/{id}", RequestId.VALUE_1.getValue())
                        .with(user(createUserDetails())))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is(newsResponseDto.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(newsResponseDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is(newsResponseDto.getAuthor())));
        verify(newsServiceService).readNewsWithComments(RequestId.VALUE_1.getValue());
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for searching news.
     * @param newsResponseDto the news response DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void searchNews(NewsResponseDto newsResponseDto) throws Exception {
        List<NewsResponseDto> newsResponseDtoList = new ArrayList<>();
        newsResponseDtoList.add(newsResponseDto);

        LocalDateTime now = LocalDateTime.now();
        when(newsServiceService.searchNews(RequestName.NAME.getValue(), now)).thenReturn(newsResponseDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/news/search")
                        .with(user(createUserDetails()))
                .param("query", (RequestName.NAME.getValue()))
                .param("date", String.valueOf(now)))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]text", Matchers.is(newsResponseDto.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]title", Matchers.is(newsResponseDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]author", Matchers.is(newsResponseDto.getAuthor())));
        verify(newsServiceService).searchNews(RequestName.NAME.getValue(), now);
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for reading all news.
     * @param newsResponseDto the news response DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void readAll(NewsResponseDto newsResponseDto) throws Exception {
        List<NewsResponseDto> newsResponseDtoList = new ArrayList<>();
        newsResponseDtoList.add(newsResponseDto);

        when(newsServiceService.readAll(Pageable.ofSize(10).withPage(0))).thenReturn(newsResponseDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/news")
                        .with(user(createUserDetails())))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]text", Matchers.is(newsResponseDto.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]title", Matchers.is(newsResponseDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]author", Matchers.is(newsResponseDto.getAuthor())));
        verify(newsServiceService).readAll(Pageable.ofSize(10).withPage(0));
        SecurityContextHolder.clearContext();
    }

    /**
     * Returns the JSON content for the given news request DTO.
     * @param newsRequestDto the news request DTO object
     * @return the JSON content as a string
     */

    @NotNull
    private String getContent(NewsRequestDto newsRequestDto) {
        return "{\n" +
                "  \"title\": \"" + newsRequestDto.getTitle() + "\",\n" +
                "  \"text\": \"" + newsRequestDto.getText() + "\"\n" +
                "}";
    }

    /**
     * Creates and returns a user details object for testing.
     * @return the user details object
     */

    private UserDetails createUserDetails(){
        return User.withUsername("username")
                .password("password")
                .roles("USER")
                .build();
    }
}
