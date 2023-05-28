package ru.clevertec.NewsManager.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.CommentRepository;
import ru.clevertec.NewsManager.service.news.NewsService;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentApiService implements CommentService{

    private final CommentRepository commentRepository;
    private final NewsService newsService;

    @Override
    public long create(CommentRequestDto comment) {
        News news = newsService.read(comment.getNewsId());
        Comment builderComment = builderCreateComment(comment);
        builderComment.setNews(news);
        return commentRepository.save(builderComment).getId();
    }

    @Override
    public Comment read(long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid Comment Id:" + id));
    }

    @Override
    public boolean update(CommentRequestDto comment, Long id) {
        Comment readComment = read(id);
        LocalDateTime time = readComment.getTime();
        readComment.setId(id);
        Comment builderUpdateComment = builderUpdateComment(comment, time);
        commentRepository.save(builderUpdateComment);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        read(id);
        commentRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Comment> readAll(Pageable pageable) {
        return  commentRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Comment> searchComments(String query, LocalDate date) {
        return commentRepository.searchComments(query, date);
    }

    private Comment builderCreateComment(CommentRequestDto commentRequestDto){
        return Comment.builder()
                .time(LocalDateTime.now())
                .text(commentRequestDto.getText())
                .username(commentRequestDto.getUsername())
                .build();
    }

    private Comment builderUpdateComment(CommentRequestDto commentRequestDto, LocalDateTime data){
        return Comment.builder()
                .time(data)
                .text(commentRequestDto.getText())
                .username(commentRequestDto.getUsername())
                .build();
    }
}
