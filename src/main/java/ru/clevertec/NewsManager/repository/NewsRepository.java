package ru.clevertec.NewsManager.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.NewsManager.entity.News;

import java.time.LocalDateTime;
import java.util.List;

/**
 The NewsRepository interface is a repository that provides data access and database operations
 for the News entity in the NewsManager application.
 */

public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     Retrieves a specific news article with its associated comments.
     @param newsId the ID of the news article
     @return the news article with comments
     */

    @EntityGraph(attributePaths = "comment")
    @Query("SELECT n FROM News n WHERE n.id = :newsId")
    News findNewsWithComments(@Param("newsId") Long newsId);

    /**
     Searches for news articles based on the provided query string.
     @param query the search query
     @return the list of news articles matching the query
     */

    @Query(value = "SELECT * FROM news WHERE (:query IS NULL OR (title || ' ' || text) @@ to_tsquery(:query))", nativeQuery = true)
    List<News> searchNewsByQuery(@Param("query") String query);

    /**
     Searches for news articles published on the specified date.
     @param date the date to search for news articles
     @return the list of news articles published on the given date
     */

    @Query("SELECT n FROM News n WHERE n.time = :date")
    List<News> searchNewsByDate(@Param("date") LocalDateTime date);

}
