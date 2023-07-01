package ru.clevertec.NewsManager.common.controller;

import org.hamcrest.Matchers;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.NewsManager.NewsManagerApplication;

import ru.clevertec.NewsManager.common.WireMockInitializer;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverCommentsRequestDto;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverNewsRequestDto;
import ru.clevertec.NewsManager.common.utill.RequestId;
import ru.clevertec.NewsManager.controller.CommentController;
import ru.clevertec.NewsManager.dto.request.CommentRequestProtos;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.security.JwtTokenGenerator;
import ru.clevertec.NewsManager.security.SecurityConfig;
import ru.clevertec.NewsManager.service.comment.CommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.NewsManager.common.utill.CommentBuilder.builderComment;
import static ru.clevertec.NewsManager.common.utill.CommentBuilder.createUserDetails;
import static ru.clevertec.NewsManager.common.utill.CommentBuilder.getContent;

/**
 * Class for testing the CommentController.
 */
@ContextConfiguration(classes = NewsManagerApplication.class)
@WebMvcTest({CommentController.class, SecurityConfig.class, JwtTokenGenerator.class})
@ExtendWith({ValidParameterResolverNewsRequestDto.class, ValidParameterResolverCommentsRequestDto.class})
@AutoConfigureMockMvc
@DisplayName("Testing Comments Controller")
public class CommentControllerTest {


    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Method executed before running all the tests.
     */

    @BeforeAll
    static void setup() {
        WireMockInitializer.setup();
    }

    @AfterAll
    static void teardown() {
        WireMockInitializer.teardown();
    }

    /**
     * Test for creating a comment.
     * @param commentRequestDto the comment request DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void create(CommentRequestProtos.CommentRequestDto commentRequestDto) throws Exception {
        when(commentService.create(any(CommentRequestProtos.CommentRequestDto.class))).thenReturn(RequestId.VALUE_1.getValue());
        mockMvc.perform(MockMvcRequestBuilders.post("/comment")
                        .with(user(createUserDetails()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(commentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(RequestId.VALUE_1.getValue())));
        verify(commentService).create(any(CommentRequestProtos.CommentRequestDto.class));
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for updating a comment.
     * @param commentRequestDto the comment request DTO object
     * @throws Exception if an exception occurs during the test
     */
    @Test
    public void update(CommentRequestProtos.CommentRequestDto commentRequestDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/comment/{id}", RequestId.VALUE_1.getValue())
                        .with(user(createUserDetails()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(commentRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(commentService).update(any(CommentRequestProtos.CommentRequestDto.class), any());
        SecurityContextHolder.clearContext();
    }


    /**
     * Test for deleting a comment.
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{id}", RequestId.VALUE_1.getValue())
                        .with(user(createUserDetails())))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(commentService).delete(RequestId.VALUE_1.getValue());
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for reading a comment.
     * @param commentRequestDto the comment request DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void read(CommentRequestProtos.CommentRequestDto commentRequestDto) throws Exception {
        Comment comment = builderComment(commentRequestDto);
        when(commentService.read(RequestId.VALUE_1.getValue())).thenReturn(comment);
        mockMvc.perform(MockMvcRequestBuilders.get("/comment/{id}", RequestId.VALUE_1.getValue())
                .with(user(createUserDetails())))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is(comment.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is(comment.getUsername())));
        verify(commentService).read(RequestId.VALUE_1.getValue());
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for searching comments.
     * @param commentRequestDto the comment request DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void searchComments(CommentRequestProtos.CommentRequestDto commentRequestDto) throws Exception {
        Comment comment = builderComment(commentRequestDto);
        List<Comment> commentsList = new ArrayList<>();
        commentsList.add(comment);
        String query = "name";
        LocalDateTime now = LocalDateTime.now();
        when(commentService.searchComments(query,now)).thenReturn(commentsList);
        mockMvc.perform(MockMvcRequestBuilders.get("/comment/search")
                        .param("query", (query))
                        .param("date", String.valueOf(now))
                        .with(user(createUserDetails())))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]text", Matchers.is(comment.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]username", Matchers.is(comment.getUsername())));
        verify(commentService).searchComments(query, now);
        SecurityContextHolder.clearContext();
    }

    /**
     * Test for reading all comments.
     * @param commentRequestDto the comment request DTO object
     * @throws Exception if an exception occurs during the test
     */

    @Test
    public void readAll(CommentRequestProtos.CommentRequestDto commentRequestDto) throws Exception {
        Comment comment = builderComment(commentRequestDto);
        List<Comment> commentsList = new ArrayList<>();
        commentsList.add(comment);
        when(commentService.readAll(Pageable.ofSize(10).withPage(0))).thenReturn(commentsList);
        mockMvc.perform(MockMvcRequestBuilders.get("/comment")
                        .with(user(createUserDetails())))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]text", Matchers.is(comment.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]username", Matchers.is(comment.getUsername())));
        verify(commentService).readAll(Pageable.ofSize(10).withPage(0));
        SecurityContextHolder.clearContext();
    }
}


