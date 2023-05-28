package ru.clevertec.NewsManager.service.comment;



import org.springframework.data.domain.Pageable;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;


import java.time.LocalDate;
import java.util.List;

public interface CommentService {

    long create(CommentRequestDto comment);
    Comment read (long id);
    boolean update (CommentRequestDto comment, Long id);
    boolean delete (Long id);
    List<Comment> readAll (Pageable pageable);
    List<Comment> searchComments(String query, LocalDate date);
}
