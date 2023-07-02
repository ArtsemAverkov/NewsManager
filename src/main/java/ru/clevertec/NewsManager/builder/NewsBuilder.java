package ru.clevertec.NewsManager.builder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.NewsManager.dto.request.NewsRequestProtos;
import ru.clevertec.NewsManager.dto.response.CommentResponseProtos;
import ru.clevertec.NewsManager.dto.response.NewsResponseProtos;
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
    public static List<NewsResponseProtos.NewsResponseDto> buildNewsResponseList(List<News> newsList) {
        List<NewsResponseProtos.NewsResponseDto> newsResponseDtoList = new ArrayList<>();
        for (News news : newsList) {
            NewsResponseProtos.NewsResponseDto newsResponseDto = buildNewsResp(news);
            newsResponseDtoList.add(newsResponseDto);
        }
        return newsResponseDtoList;
    }

    /**
     The buildCommentResponse method is used to build a CommentResponseDto object based on a Comment object.
     It takes a Comment object as input and returns the corresponding CommentResponseDto object.
     @param comment The Comment object to build the CommentResponseDto for.
     @return The CommentResponseDto object constructed from the Comment object.
     */
    public static CommentResponseProtos.CommentResponseDto buildCommentResponse(Comment comment) {
        return CommentResponseProtos.CommentResponseDto.newBuilder()
                .setId(comment.getId())
                .setTime(String.valueOf(comment.getTime()))
                .setText(comment.getText())
                .setUsername(comment.getUsername())
                .build();
    }

    /**
     * Builds a new News object for creating a news.
     *
     * @param newsRequestDto the NewsRequestDto containing the news details
     * @return the built News object
     */
    public static News buildCreateNews(NewsRequestProtos.NewsRequestDto newsRequestDto) {
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
    public static News buildUpdateNews(NewsRequestProtos.NewsRequestDto newsRequestDto, LocalDateTime time) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return News.builder()
                .time(time)
                .text(newsRequestDto.getText())
                .title(newsRequestDto.getTitle())
                .author(authentication.getName())
                .build();
    }

    /**
     The buildNewsResp method is used to build a NewsResponseDto object based on a News object.
     It takes a News object as input and returns the corresponding NewsResponseDto object.
     @param news The News object to build the NewsResponseDto for.
     @return The NewsResponseDto object constructed from the News object.
     */
    public static NewsResponseProtos.NewsResponseDto buildNewsResp(News news) {
        List<CommentResponseProtos.CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : news.getComment()) {
            commentResponseDtoList.add(buildCommentResponse(comment));
        }

        return NewsResponseProtos.NewsResponseDto.newBuilder()
                .setId(news.getId())
                .setAuthor(news.getAuthor())
                .setTime(String.valueOf(news.getTime()))
                .setTitle(news.getTitle())
                .setText(news.getText())
                .addAllComments(commentResponseDtoList)
                .build();
    }
}