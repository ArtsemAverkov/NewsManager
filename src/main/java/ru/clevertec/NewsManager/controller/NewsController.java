package ru.clevertec.NewsManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.NewsManager.dto.request.NewsRequestProtos;
import ru.clevertec.NewsManager.dto.response.NewsResponseProtos;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.service.news.NewsService;
import ru.clevertec.controllerlogspringbootstarter.aop.loger.IncludeLog;
import ru.clevertec.exceptionhandlerspringbootstarter.EnableExceptionHandling;

import java.time.LocalDateTime;
import java.util.List;

/**
 The NewsController class is a RESTful controller that handles HTTP requests related to news articles.
 It defines endpoints for creating, reading, updating, and deleting news articles,
 as well as searching for news articles and retrieving all news articles.
 */
@RestController
@RequestMapping(value = "/news")
@RequiredArgsConstructor
@EnableExceptionHandling
@IncludeLog
public class NewsController {

    private final NewsService newsService;

    /**
     Handles the HTTP POST request to create a new news article.
     @param news the request body containing the news article details
     @return the ID of the created news article
     */
    @PreAuthorize("hasRole('JOURNALIST')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long  create(@RequestBody @Valid NewsRequestProtos.NewsRequestDto news){
        return newsService.create(news);
    }

    /**
     Handles the HTTP GET request to retrieve a specific news article with its associated comments.
     @return the response containing the news article details and its comments
      * @param id the ID of the news article to retrieve
     */

    @GetMapping(value= "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public NewsResponseProtos.NewsResponseDto readWithComments(@PathVariable  @Valid Long id) {
       return newsService.readNewsWithComments(id);
    }

    /**
     Handles the HTTP GET request to search for news articles based on query parameters.
     @param query the search query string (optional)
     @param date the date to filter news articles (optional)
     @return the list of news articles matching the search criteria
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<NewsResponseProtos.NewsResponseDto> searchNews(@RequestParam(required = false) String query,
                                                               @RequestParam(required = false) LocalDateTime date) {
        return newsService.searchNews(query, date);
    }

    /**
     Handles the HTTP PATCH request to update an existing news article.
     @param id the ID of the news article to update
     @param news the request body containing the updated news article details
     */
    @PreAuthorize("hasRole('JOURNALIST')")
    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable @Valid Long id, @RequestBody @Valid NewsRequestProtos.NewsRequestDto news){
         newsService.update(news, id);
    }

    /**
     Handles the HTTP DELETE request to delete a specific news article.
     @param id the ID of the news article to delete
     */
    @PreAuthorize("hasRole('JOURNALIST')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Valid Long id){
         newsService.delete(id);
    }

    /**
     Handles the HTTP GET request to retrieve all news articles.
     @param pageable the pageable information for pagination and sorting
     @return the list of all news articles
     */
    @GetMapping
    public List<News>  readAll(@PageableDefault Pageable pageable){
        return newsService.readAll(pageable);
    }
}
