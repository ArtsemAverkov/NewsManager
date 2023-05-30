package ru.clevertec.NewsManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.response.NewsResponseDto;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.service.news.NewsService;

import java.time.LocalDate;
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
    public List<News> searchNews(@RequestParam(required = false) String query,
                                 @RequestParam(required = false) LocalDate date) {
        return newsService.searchNews(query, date);
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean update(@PathVariable @Valid Long id, @RequestBody @Valid NewsRequestDto news){
        return newsService.update(news, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable @Valid Long id){
        return newsService.delete(id);
    }

    @GetMapping
    public List<NewsResponseDto> readAll(@PageableDefault Pageable pageable){
        return newsService.readAll(pageable);
    }
}
