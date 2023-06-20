package ru.clevertec.NewsManager.service.news;

import org.springframework.data.domain.Pageable;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.responseNews.NewsResponseDto;
import ru.clevertec.NewsManager.entity.News;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {
    long create(NewsRequestDto news);
    News read (long id);
    void update (NewsRequestDto news, Long id);
    void delete (Long id);
    List<NewsResponseDto> readAll (Pageable pageable);
    NewsResponseDto readNewsWithComments(Long id);
    List<NewsResponseDto> searchNews(String query, LocalDateTime date);
}
