package ru.clevertec.NewsManager.service.news;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.NewsManager.builder.NewsBuilder;
import ru.clevertec.NewsManager.dto.request.NewsRequestProtos;
import ru.clevertec.NewsManager.dto.response.NewsResponseProtos;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.clevertec.NewsManager.builder.NewsBuilder.buildNewsResp;

/**
 The NewsApiService class implements the NewsService interface and provides
 the functionality to manage news in the NewsManager application.
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "newsCache")
public class NewsApiService implements NewsService {

    private final NewsRepository newsRepository;

    /**
     Creates a new news based on the provided news request DTO.
     @param news the news request DTO
     @return the ID of the created news
     */
    @CachePut(value = "newsCache", key = "#result")
    @Override
    public Long create(NewsRequestProtos.NewsRequestDto news) {
        News buildCreateNews = NewsBuilder.buildCreateNews(news);
        return newsRepository.save(buildCreateNews).getId();
    }

    /**
     Retrieves a news by its ID.
     @param id the ID of the news
     @return the retrieved news
     @throws IllegalArgumentException if the news ID is invalid
     */
    @Cacheable(cacheNames = "newsCache", key = "#id", unless = "#result == null")
    @Override
    public News read(long id) {
        return newsRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Invalid News id:" + id));
    }

    /**
     Retrieves a news with its associated comments by the news ID.
     @return the news response DTO with associated comments
      * @param id the ID of the news
     */
    @Override
    public NewsResponseProtos.NewsResponseDto readNewsWithComments(Long id){
        read(id);
        News newsWithComments = newsRepository.findNewsWithComments(id);
        return buildNewsResp(newsWithComments);
    }

    /**
     Searches for news based on the provided query and date.
     @return the list of matching news response DTOs
      * @param query the search query
     * @param date the search date
     */
    public List<NewsResponseProtos.NewsResponseDto> searchNews(String query, LocalDateTime date) {
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
    @CacheEvict(value = "newsCache", key = "#id")
    @CachePut(value = "newsCache", key = "#id")
    @Override
    @Transactional
    public void update(NewsRequestProtos.NewsRequestDto news, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        News readNews = read(id);
        if (readNews.getAuthor().equals(authentication.getName())) {
            LocalDateTime timeCreateNews = readNews.getTime();
            News buildUpdateNews = NewsBuilder.buildUpdateNews(news, timeCreateNews);
            buildUpdateNews.setId(id);
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
    @CacheEvict(value = "newsCache", key = "#id")
    @Override
    @Transactional
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
    public List<News> readAll(Pageable pageable) {
        return newsRepository.findAll(pageable).getContent();
    }
}
