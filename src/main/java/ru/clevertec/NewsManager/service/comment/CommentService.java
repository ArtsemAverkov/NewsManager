package ru.clevertec.NewsManager.service.comment;

import org.springframework.data.domain.Pageable;
import ru.clevertec.NewsManager.dto.request.CommentRequestProtos;
import ru.clevertec.NewsManager.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    Long create(CommentRequestProtos.CommentRequestDto comment);
    Comment read (long id);
    void update (CommentRequestProtos.CommentRequestDto comment, Long id);
    void delete (Long id);
    List<Comment> readAll (Pageable pageable);
    List<Comment> searchComments(String query, LocalDateTime date);
}
