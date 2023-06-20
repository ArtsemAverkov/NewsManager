package ru.clevertec.NewsManager.dto.responseNews;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor

public class NewsResponseDto {
    private LocalDateTime time;
    private String title;
    private String text;
    private String author;
    private List<CommentResponseDto> comments;
}
