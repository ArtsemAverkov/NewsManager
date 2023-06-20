package ru.clevertec.NewsManager.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private Long newsId;
    private String text;
}
