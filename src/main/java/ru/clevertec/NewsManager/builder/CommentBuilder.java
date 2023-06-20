package ru.clevertec.NewsManager.builder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;

import java.time.LocalDateTime;

/**

 This class provides static methods to build Comment objects based on CommentRequestDto.
 */

public class CommentBuilder {

    /**
     * Builds a new Comment object for creating a comment.
     *
     * @param commentRequestDto the CommentRequestDto containing the comment details
     * @return the built Comment object
     */

    public static Comment buildCreateComment(CommentRequestDto commentRequestDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    LocalDateTime now = LocalDateTime.now().withNano(0);
    return Comment.builder()
            .time(now)
            .text(commentRequestDto.getText())
            .username(authentication.getName())
            .build();
    }

    /**
     * Builds a new Comment object for updating a comment.
     *
     * @param commentRequestDto the CommentRequestDto containing the updated comment details
     * @param data              the LocalDateTime representing the updated time of the comment
     * @return the built Comment object
     */

    public static Comment buildUpdateComment(CommentRequestDto commentRequestDto, LocalDateTime data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Comment.builder()
                .time(data)
                .text(commentRequestDto.getText())
                .username(authentication.getName())
                .build();
    }
}