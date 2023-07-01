package ru.clevertec.NewsManager.common.utill;

import org.jetbrains.annotations.NotNull;
import ru.clevertec.NewsManager.dto.request.CommentRequestProtos;
import ru.clevertec.NewsManager.dto.request.NewsRequestProtos;
import ru.clevertec.NewsManager.dto.response.CommentResponseProtos;
import ru.clevertec.NewsManager.dto.response.NewsResponseProtos;
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
    public static List<NewsResponseProtos.NewsResponseDto> buildResponseList (List<News> news){

        List<NewsResponseProtos.NewsResponseDto> newsResponseDtoList = new ArrayList<>();
        for (News response :news) {
            List<CommentResponseProtos.CommentResponseDto> commentResponseDtoList
                    = convertComment(response.getComment());
            NewsResponseProtos.NewsResponseDto build = NewsResponseProtos.NewsResponseDto.newBuilder()
                    .setId(response.getId())
                    .setTime(String.valueOf(response.getTime()))
                    .setText(response.getText())
                    .setTitle(response.getTitle())
                    .setAuthor(response.getAuthor())
                    .addAllComments(commentResponseDtoList)
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
    public static NewsResponseProtos.NewsResponseDto buildResponse (News news){
        List<CommentResponseProtos.CommentResponseDto> commentResponseDtoList
                = convertComment(news.getComment());
        return NewsResponseProtos.NewsResponseDto.newBuilder()
                .setId(news.getId())
                .setTime(String.valueOf(news.getTime()))
                .setText(news.getText())
                .setTitle(news.getTitle())
                .setAuthor(news.getAuthor())
                .addAllComments(commentResponseDtoList)
                .build();
    }

    /**
     * Converts a list of Comment entities to a list of CommentResponseDto objects.
     * @param comments the list of Comment entities
     * @return the list of CommentResponseDto objects
     */
    public static List<CommentResponseProtos.CommentResponseDto> convertComment(List<Comment> comments){
        List<CommentResponseProtos.CommentResponseDto> commentResponseDto = new ArrayList<>();
        for (Comment comment : comments){
            CommentResponseProtos.CommentResponseDto build = CommentResponseProtos.CommentResponseDto.newBuilder()
                    .setId(comment.getId())
                    .setTime(String.valueOf(comment.getTime()))
                    .setText(comment.getText())
                    .setUsername(comment.getUsername())
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
    public static News buildCreateNews(NewsRequestProtos.NewsRequestDto news){
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
    public static List<Comment> getComments(CommentRequestProtos.CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setId(commentRequestDto.getNewsId());
        comment.setUsername("user");

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
    public static News getNews(NewsRequestProtos.NewsRequestDto news, CommentRequestProtos.CommentRequestDto commentRequestDto) {
        List<Comment> commentList = getComments(commentRequestDto);
        News newsCreate = buildCreateNews(news);
        newsCreate.setComment(commentList);
        return newsCreate;
    }
}
