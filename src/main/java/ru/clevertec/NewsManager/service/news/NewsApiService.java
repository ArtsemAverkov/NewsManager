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

/**
 The NewsApiService class implements the NewsService interface and provides
 the functionality to manage news in the NewsManager application.
 */

@Service
@RequiredArgsConstructor
public class NewsApiService implements NewsService {

    private final NewsRepository newsRepository;

    /**
     Creates a new news based on the provided news request DTO.
     @param news the news request DTO
     @return the ID of the created news
     */

    @Cacheable("myCache")
    @Override
    public long create(NewsRequestDto news) {
        News buildCreateNews = NewsBuilder.buildCreateNews(news);
        return newsRepository.save(buildCreateNews).getId();
    }

    /**
     Retrieves a news by its ID.
     @param id the ID of the news
     @return the retrieved news
     @throws IllegalArgumentException if the news ID is invalid
     */

    @Cacheable("myCache")
    @Override
    public News read(long id) {
        return newsRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Invalid News id:" + id));
    }

    /**
     Retrieves a news with its associated comments by the news ID.
     @param id the ID of the news
     @return the news response DTO with associated comments
     */

    @Override
    public NewsResponseDto readNewsWithComments(Long id){
        read(id);
        return NewsBuilder.buildNewsResponse(newsRepository.findNewsWithComments(id));
    }

    /**
     Searches for news based on the provided query and date.
     @param query the search query
     @param date the search date
     @return the list of matching news response DTOs
     */

    public List<NewsResponseDto> searchNews(String query, LocalDateTime date) {
        if (Objects.nonNull(query)){
            return NewsBuilder.buildNewsResponseList(newsRepository.searchNewsByQuery(query));
        }
            return NewsBuilder.buildNewsResponseList(newsRepository.searchNewsByDate(date));
    }

    /**
     Updates a news with the provided news request DTO and ID.
     @param news the news request DTO
     @param id the ID of the news to update
     @throws AccessDeniedException if the current user is not the author of the news
     */

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

    /**
     Deletes a news by its ID.
     @param id the ID of the news to delete
     @throws AccessDeniedException if the current user is not the author of the news
     */

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

    /**
     Retrieves all news with pagination.
     @param pageable the pagination information
     @return the list of retrieved news response DTOs
     */

    @Override
    public List<NewsResponseDto> readAll(Pageable pageable) {
        return NewsBuilder.buildNewsResponseList(newsRepository.findAll(pageable).getContent());
    }
}
