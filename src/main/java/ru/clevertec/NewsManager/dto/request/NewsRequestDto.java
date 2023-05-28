package ru.clevertec.NewsManager.dto.request;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequestDto {
    private String title;
    private String text;
}
