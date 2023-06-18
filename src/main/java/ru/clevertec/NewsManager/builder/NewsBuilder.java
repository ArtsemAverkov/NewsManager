package ru.clevertec.NewsManager.builder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.responseNews.CommentResponseDto;
import ru.clevertec.NewsManager.dto.responseNews.NewsResponseDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewsBuilder {

    public static List<NewsResponseDto> buildNewsResponseList(List<News> newsList) {
    List<NewsResponseDto> newsResponseDtoList = new ArrayList<>();
    for (News news : newsList) {
        NewsResponseDto newsResponseDto = buildNewsResponse(news);
        newsResponseDtoList.add(newsResponseDto);
    }
    return newsResponseDtoList;
}

    public static NewsResponseDto buildNewsResponse(News news) {
        List<CommentResponseDto> commentResponseDtoList = convertComment(news.getComment());

        return NewsResponseDto.builder()
                .time(news.getTime())
                .text(news.getText())
                .title(news.getTitle())
                .author(news.getAuthor())
                .comments(commentResponseDtoList)
                .build();
    }

    public static List<CommentResponseDto> convertComment(List<Comment> comments) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDto commentResponseDto = buildCommentResponse(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    public static CommentResponseDto buildCommentResponse(Comment comment) {
        return CommentResponseDto.builder()
                .time(comment.getTime())
                .text(comment.getText())
                .username(comment.getUsername())
                .build();
    }

    public static News buildCreateNews(NewsRequestDto newsRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LocalDateTime now = LocalDateTime.now();
        return News.builder()
                .time(now)
                .title(newsRequestDto.getTitle())
                .text(newsRequestDto.getText())
                .author(authentication.getName())
                .build();
    }

    public static News buildUpdateNews(NewsRequestDto newsRequestDto, LocalDateTime time) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return News.builder()
                .time(time)
                .text(newsRequestDto.getText())
                .title(newsRequestDto.getTitle())
                .author(authentication.getName())
                .build();
    }
}
