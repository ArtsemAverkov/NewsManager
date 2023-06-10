package ru.clevertec.NewsManager.service.news;

import org.springframework.data.domain.Pageable;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.response.NewsResponseDto;
import ru.clevertec.NewsManager.entity.News;


import java.time.LocalDate;
import java.util.List;

public interface NewsService {
    long create(NewsRequestDto news);
    News read (long id);
    void update (NewsRequestDto news, Long id);
    void delete (Long id);
    List<News> readAll (Pageable pageable);
    NewsResponseDto readNewsWithComments(Long id);
    List<News> searchNews(String query, LocalDate date);
}
