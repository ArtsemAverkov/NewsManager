package ru.clevertec.NewsManager.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private LocalDateTime time;
    private String text;
    private String username;
}
