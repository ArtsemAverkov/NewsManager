package ru.clevertec.NewsManager.common.utill;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.NewsManager.common.utill.RequestId;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;

import java.time.LocalDateTime;

/**
 * This class provides utility methods for building Comment objects and creating UserDetails.
 */
public class CommentBuilder {

    /**
     * Builds and returns a Comment object based on the given CommentRequestDto.
     * @param commentRequestDto the CommentRequestDto containing the comment details
     * @return the built Comment object
     */
    public static Comment builderComment(CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .id(RequestId.VALUE_1.getValue())
                .time(LocalDateTime.now())
                .text(commentRequestDto.getText())
                .username("user")
                .build();
    }

    /**
     * Creates and returns a UserDetails object with a predefined username, password, and role.
     * @return the created UserDetails object
     */
    public static UserDetails createUserDetails(){
        return User.withUsername("username")
                .password("password")
                .roles("USER")
                .build();
    }

    /**
     * Gets the content of the CommentRequestDto in a specific format.
     * @param commentRequestDto the CommentRequestDto to get the content from
     * @return the content in the specified format
     */
    @NotNull
    public static  String getContent(CommentRequestDto commentRequestDto) {
        return "{\n" +
                "  \"newsId\": " + commentRequestDto.getNewsId() + ",\n" +
                "  \"text\": \"" + commentRequestDto.getText() + "\"\n" +
                "}";
    }
}
