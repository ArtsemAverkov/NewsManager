package ru.clevertec.NewsManager.service.news;

import org.springframework.data.domain.Pageable;
import ru.clevertec.NewsManager.dto.request.NewsRequestProtos;
import ru.clevertec.NewsManager.dto.response.NewsResponseProtos;
import ru.clevertec.NewsManager.entity.News;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {
    long create(NewsRequestProtos.NewsRequestDto news);
    News read (long id);
    void update (NewsRequestProtos.NewsRequestDto news, Long id);
    void delete (Long id);
    List<News>  readAll (Pageable pageable);
    NewsResponseProtos.NewsResponseDto readNewsWithComments(Long id);
    List<NewsResponseProtos.NewsResponseDto> searchNews(String query, LocalDateTime date);
}
