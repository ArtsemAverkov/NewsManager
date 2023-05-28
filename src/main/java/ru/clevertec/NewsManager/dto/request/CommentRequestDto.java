package ru.clevertec.NewsManager.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private Long newsId;
    private String text;
    private String username;
}
