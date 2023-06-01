package ru.clevertec.NewsManager.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class NewsResponseDto {
    private LocalDateTime time;
    private String title;
    private String text;
    private String author;
    private List<CommentResponseDto> comments;
}
