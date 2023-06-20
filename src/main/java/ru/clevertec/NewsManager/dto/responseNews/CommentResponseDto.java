package ru.clevertec.NewsManager.dto.responseNews;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class CommentResponseDto {
    private LocalDateTime time;
    private String text;
    private String username;
}
