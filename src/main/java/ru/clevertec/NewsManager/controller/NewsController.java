package ru.clevertec.NewsManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.responseNews.NewsResponseDto;
import ru.clevertec.NewsManager.service.news.NewsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/news")
@RequiredArgsConstructor
public class NewsController {



    private final NewsService newsService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid NewsRequestDto news){
        return newsService.create(news);
    }

    @GetMapping(value= "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsResponseDto readWithComments(@PathVariable  @Valid Long id) {
        return newsService.readNewsWithComments(id);
    }

    @GetMapping(value = "/search")
    @ResponseStatus(HttpStatus.OK)
    public List<NewsResponseDto> searchNews(@RequestParam(required = false) String query,
                                 @RequestParam(required = false) LocalDateTime date) {
        return newsService.searchNews(query, date);
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable @Valid Long id, @RequestBody @Valid NewsRequestDto news){
         newsService.update(news, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Valid Long id){
         newsService.delete(id);
    }

    @GetMapping
    public List<NewsResponseDto> readAll(@PageableDefault Pageable pageable){
        return newsService.readAll(pageable);
    }
}
