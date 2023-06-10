package ru.clevertec.NewsManager.service.news;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.clevertec.NewsManager.aop.cache.Cacheable;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.response.CommentResponseDto;
import ru.clevertec.NewsManager.dto.response.NewsResponseDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.NewsRepository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsApiService implements NewsService {

    private final NewsRepository newsRepository;


    @Cacheable("myCache")
    @Override
    public long create(NewsRequestDto news) {
        News buildCreateNews = buildCreateNews(news);
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
        return buildResponse(newsRepository.findNewsWithComments(id));
    }

    public List<News> searchNews(String query, LocalDate date) {
            return newsRepository.searchNews(query, date);
    }


    @Cacheable("myCache")
    @Override
    public void update(NewsRequestDto news, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        News readNews = read(id);
        if (readNews.getAuthor().equals(authentication.getName())) {
            readNews.setId(id);
            LocalDateTime timeCreateNews = readNews.getTime();
            News buildUpdateNews = buildUpdateNews(news, timeCreateNews);
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
    public List<News> readAll(Pageable pageable) {
        return newsRepository.findAll(pageable).getContent();
    }

    public  List<NewsResponseDto> buildResponseList (List<News> news){
        List<NewsResponseDto> newsResponseDtoList = new ArrayList<>();
        for (News response :news) {
            NewsResponseDto build = NewsResponseDto.builder()
                    .time(response.getTime())
                    .text(response.getText())
                    .title(response.getTitle())
                    .author(response.getAuthor())
                    .comments(convertComment(response.getComment()))
                    .build();
        newsResponseDtoList.add(build);
        }
        return newsResponseDtoList;
    }

    public NewsResponseDto buildResponse (News news){
        return NewsResponseDto.builder()
                .time(news.getTime())
                .text(news.getText())
                .title(news.getTitle())
                .author(news.getAuthor())
                .comments(convertComment(news.getComment()))
                .build();
    }

    public List<CommentResponseDto> convertComment(List<Comment> comments){
        List<CommentResponseDto> commentResponseDto = new ArrayList<>();
        for (Comment comment : comments){
            CommentResponseDto build = CommentResponseDto.builder()
                    .time(comment.getTime())
                    .text(comment.getText())
                    .username(comment.getUsername())
                    .build();
            commentResponseDto.add(build);
        }
        return commentResponseDto;
    }

    private News buildCreateNews(NewsRequestDto news){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LocalDateTime now = LocalDateTime.now();
        return News.builder()
                .time(now)
                .title(news.getTitle())
                .text(news.getText())
                .author(authentication.getName())
                .build();
    }

    private News buildUpdateNews(NewsRequestDto news, LocalDateTime time){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return News.builder()
                .time(time)
                .text(news.getText())
                .title(news.getTitle())
                .author(authentication.getName())
                .build();
    }
}
