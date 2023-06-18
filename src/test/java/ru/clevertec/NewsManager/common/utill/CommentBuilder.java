package ru.clevertec.NewsManager.common.utill;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.NewsManager.common.utill.RequestId;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;

import java.time.LocalDateTime;

public class CommentBuilder {

    public static Comment builderComment(CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .id(RequestId.VALUE_1.getValue())
                .time(LocalDateTime.now())
                .text(commentRequestDto.getText())
                .username("user")
                .build();
    }

    public static UserDetails createUserDetails(){
        return User.withUsername("username")
                .password("password")
                .roles("USER")
                .build();
    }

    @NotNull
    public static  String getContent(CommentRequestDto commentRequestDto) {
        return "{\n" +
                "  \"newsId\": " + commentRequestDto.getNewsId() + ",\n" +
                "  \"text\": \"" + commentRequestDto.getText() + "\"\n" +
                "}";
    }
}
