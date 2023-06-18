package ru.clevertec.NewsManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.NewsManager.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comment WHERE (:query IS NULL OR (text || ' ' || username) @@ to_tsquery(:query))", nativeQuery = true)
    List<Comment> searchCommentsByQuery(@Param("query") String query);

    @Query("SELECT n FROM Comment n WHERE n.time = :date")
    List<Comment> searchCommentsByDate(@Param("date") LocalDateTime date);

}


