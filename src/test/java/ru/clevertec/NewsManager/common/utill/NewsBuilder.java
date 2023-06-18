package ru.clevertec.NewsManager.common.utill;

import org.jetbrains.annotations.NotNull;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.responseNews.CommentResponseDto;
import ru.clevertec.NewsManager.dto.responseNews.NewsResponseDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewsBuilder {

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

    public static NewsResponseDto buildResponse (News news){
        return NewsResponseDto.builder()
                .time(news.getTime())
                .text(news.getText())
                .title(news.getTitle())
                .author(news.getAuthor())
                .comments(convertComment(news.getComment()))
                .build();
    }

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
    @NotNull
    public static List<Comment> getComments(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setId(commentRequestDto.getNewsId());

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        return commentList;
    }

    @NotNull
    public static News getNews(NewsRequestDto news, CommentRequestDto commentRequestDto) {
        List<Comment> commentList = getComments(commentRequestDto);
        News newsCreate = buildCreateNews(news);
        newsCreate.setComment(commentList);
        return newsCreate;
    }
}
