package ru.clevertec.NewsManager.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.NewsManager.entity.News;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    @EntityGraph(attributePaths = "comment")
    @Query("SELECT n FROM News n WHERE n.id = :newsId")
    News findNewsWithComments(@Param("newsId") Long newsId);

    @Query(value = "SELECT * FROM news WHERE (:query IS NULL OR (title || ' ' || text) @@ to_tsquery(:query))", nativeQuery = true)
    List<News> searchNewsByQuery(@Param("query") String query);

    @Query("SELECT n FROM News n WHERE n.time = :date")
    List<News> searchNewsByDate(@Param("date") LocalDateTime date);

}
