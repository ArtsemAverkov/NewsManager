package ru.clevertec.NewsManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.NewsManager.entity.Comment;



import java.time.LocalDate;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comment WHERE (:query IS NULL OR (text || ' ' || username) @@ to_tsquery(:query)) AND (:date IS NULL OR time = :date)", nativeQuery = true)
    List<Comment> searchComments(@Param("query") String query, @Param("date") LocalDate date);


}


