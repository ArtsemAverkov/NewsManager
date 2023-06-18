package ru.clevertec.NewsManager.builder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;

import java.time.LocalDateTime;

public class CommentBuilder {
    public static Comment buildCreateComment(CommentRequestDto commentRequestDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    LocalDateTime now = LocalDateTime.now().withNano(0);
    return Comment.builder()
            .time(now)
            .text(commentRequestDto.getText())
            .username(authentication.getName())
            .build();
}

    public static Comment buildUpdateComment(CommentRequestDto commentRequestDto, LocalDateTime data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Comment.builder()
                .time(data)
                .text(commentRequestDto.getText())
                .username(authentication.getName())
                .build();
    }
}