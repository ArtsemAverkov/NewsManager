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

/**
 This class provides static methods to build News and DTO objects.
 */

public class NewsBuilder {

    /**
     * Builds a list of NewsResponseDto objects from a list of News entities.
     * @param newsList the list of News entities
     * @return the list of NewsResponseDto objects
     */

    public static List<NewsResponseDto> buildNewsResponseList(List<News> newsList) {
    List<NewsResponseDto> newsResponseDtoList = new ArrayList<>();
    for (News news : newsList) {
        NewsResponseDto newsResponseDto = buildNewsResponse(news);
        newsResponseDtoList.add(newsResponseDto);
    }
    return newsResponseDtoList;
    }

    /**
     * Builds a NewsResponseDto object from a News entity.
     * @param news the News entity
     * @return the NewsResponseDto object
     */

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

    /**
     * Converts a list of Comment entities to a list of CommentResponseDto objects.
     * @param comments the list of Comment entities
     * @return the list of CommentResponseDto objects
     */

    public static List<CommentResponseDto> convertComment(List<Comment> comments) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDto commentResponseDto = buildCommentResponse(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    /**
     * Builds a CommentResponseDto object from a Comment entity.
     * @param comment the Comment entity
     * @return the CommentResponseDto object
     */

    public static CommentResponseDto buildCommentResponse(Comment comment) {
        return CommentResponseDto.builder()
                .time(comment.getTime())
                .text(comment.getText())
                .username(comment.getUsername())
                .build();
    }

    /**
     * Builds a new News object for creating a news.
     * @param newsRequestDto the NewsRequestDto containing the news details
     * @return the built News object
     */

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

    /**
     * Builds a new News object for updating a news.
     * @param newsRequestDto the NewsRequestDto containing the updated news details
     * @param time           the LocalDateTime representing the updated time of the news
     * @return the built News object
     */

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
