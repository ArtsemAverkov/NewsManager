package ru.clevertec.NewsManager.common.utill;

import org.jetbrains.annotations.NotNull;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.response.CommentResponseDto;
import ru.clevertec.NewsManager.dto.response.NewsResponseDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides utility methods for building News objects and converting response DTOs.
 */
public class NewsBuilder {

    /**
     * Builds a list of NewsResponseDto objects based on the provided list of News entities.
     * @param news the list of News entities
     * @return the list of NewsResponseDto objects
     */
    public static List<NewsResponseDto> buildResponseList (List<News> news){
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

    /**
     * Builds a NewsResponseDto object based on the provided News entity.
     * @param news the News entity
     * @return the NewsResponseDto object
     */
    public static NewsResponseDto buildResponse (News news){
        return NewsResponseDto.builder()
                .time(news.getTime())
                .text(news.getText())
                .title(news.getTitle())
                .author(news.getAuthor())
                .comments(convertComment(news.getComment()))
                .build();
    }

    /**
     * Converts a list of Comment entities to a list of CommentResponseDto objects.
     * @param comments the list of Comment entities
     * @return the list of CommentResponseDto objects
     */
    public static List<CommentResponseDto> convertComment(List<Comment> comments){
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

    /**
     * Builds a News entity based on the provided NewsRequestDto.
     * @param news the NewsRequestDto containing the news details
     * @return the built News entity
     */
    public static News buildCreateNews(NewsRequestDto news){
        LocalDateTime now = LocalDateTime.now();
        return News.builder()
                .id(RequestId.VALUE_1.getValue())
                .time(now)
                .title(news.getTitle())
                .text(news.getText())
                .author(RequestName.USERNAME.getValue())
                .build();
    }

    /**
     * Gets a list of Comment entities based on the provided CommentRequestDto.
     * @param commentRequestDto the CommentRequestDto
     * @return the list of Comment entities
     */
    @NotNull
    public static List<Comment> getComments(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setId(commentRequestDto.getNewsId());

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        return commentList;
    }

    /**
     * Gets a News entity based on the provided NewsRequestDto and CommentRequestDto.
     * @param news              the NewsRequestDto containing the news details
     * @param commentRequestDto the CommentRequestDto
     * @return the News entity
     */
    @NotNull
    public static News getNews(NewsRequestDto news, CommentRequestDto commentRequestDto) {
        List<Comment> commentList = getComments(commentRequestDto);
        News newsCreate = buildCreateNews(news);
        newsCreate.setComment(commentList);
        return newsCreate;
    }
}
