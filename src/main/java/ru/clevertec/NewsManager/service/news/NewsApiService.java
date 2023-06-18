package ru.clevertec.NewsManager.service.news;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.clevertec.NewsManager.aop.cache.Cacheable;
import ru.clevertec.NewsManager.builder.NewsBuilder;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.responseNews.NewsResponseDto;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NewsApiService implements NewsService {

    private final NewsRepository newsRepository;


    @Cacheable("myCache")
    @Override
    public long create(NewsRequestDto news) {
        News buildCreateNews = NewsBuilder.buildCreateNews(news);
        return newsRepository.save(buildCreateNews).getId();
    }

    @Cacheable("myCache")
    @Override
    public News read(long id) {
        return newsRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Invalid News id:" + id));
    }

    @Override
    public NewsResponseDto readNewsWithComments(Long id){
        read(id);
        return NewsBuilder.buildNewsResponse(newsRepository.findNewsWithComments(id));
    }

    public List<NewsResponseDto> searchNews(String query, LocalDateTime date) {
        if (Objects.nonNull(query)){
            return NewsBuilder.buildNewsResponseList(newsRepository.searchNewsByQuery(query));
        }
            return NewsBuilder.buildNewsResponseList(newsRepository.searchNewsByDate(date));
    }


    @Cacheable("myCache")
    @Override
    public void update(NewsRequestDto news, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        News readNews = read(id);
        if (readNews.getAuthor().equals(authentication.getName())) {
            readNews.setId(id);
            LocalDateTime timeCreateNews = readNews.getTime();
            News buildUpdateNews = NewsBuilder.buildUpdateNews(news, timeCreateNews);
            newsRepository.save(buildUpdateNews);
        } else {
            throw new AccessDeniedException(
                    "You do not have rights to update the news because you are not the author of this news");
        }
    }

    @Cacheable("myCache")
    @Override
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        News read = read(id);
        if (read.getAuthor().equals(authentication.getName())) {
            newsRepository.deleteById(id);
        } else {
            throw new AccessDeniedException(
                    "You do not have rights to delete the news because you are not the author of this news");
        }
    }

    @Override
    public List<NewsResponseDto> readAll(Pageable pageable) {
        return NewsBuilder.buildNewsResponseList(newsRepository.findAll(pageable).getContent());
    }
}
