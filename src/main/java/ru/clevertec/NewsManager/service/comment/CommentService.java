package ru.clevertec.NewsManager.service.comment;

import org.springframework.data.domain.Pageable;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    long create(CommentRequestDto comment);
    Comment read (long id);
    void update (CommentRequestDto comment, Long id);
    void delete (Long id);
    List<Comment> readAll (Pageable pageable);
    List<Comment> searchComments(String query, LocalDateTime date);
}
