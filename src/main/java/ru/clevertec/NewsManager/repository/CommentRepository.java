package ru.clevertec.NewsManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.NewsManager.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

/**
 The CommentRepository interface is a repository that provides data access and database operations
 for the Comment entity in the NewsManager application.
 */

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     Searches for comments based on the provided query string.
     @param query the search query
     @return the list of comments matching the query
     */

    @Query(value = "SELECT * FROM comment WHERE (:query IS NULL OR (text || ' ' || username) @@ to_tsquery(:query))", nativeQuery = true)
    List<Comment> searchCommentsByQuery(@Param("query") String query);

    /**
     Searches for comments posted on the specified date.
     @param date the date to search for comments
     @return the list of comments posted on the given date
     */

    @Query("SELECT n FROM Comment n WHERE n.time = :date")
    List<Comment> searchCommentsByDate(@Param("date") LocalDateTime date);

}


