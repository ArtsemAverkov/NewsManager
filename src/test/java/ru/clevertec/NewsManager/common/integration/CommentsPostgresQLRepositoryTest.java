package ru.clevertec.NewsManager.common.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverCommentsRequestDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.repository.CommentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 Integration tests for the CommentsPostgresQLRepository class.
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(ValidParameterResolverCommentsRequestDto.class)
@DisplayName("Comments Repository Test")
public class CommentsPostgresQLRepositoryTest extends TestContainerInitializer{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    /**
     * Test for searching comments with a query parameter.
     */
    @Test

    void shouldSearchCommentsHasParameterQuery(){
        String query = "Great";
        List<Comment> comments = commentRepository.searchCommentsByQuery(query);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        for (Comment comment : comments) {
            Assertions
                    .assertThat("Great news! Can't wait to see how this new technology unfolds.")
                    .isEqualTo(comment.getText());
        }
    }

    /**
     * Test for searching comments with a date parameter.
     */
    @Test
    @Transactional
    void shouldSearchCommentsHasParameterDate(){
        LocalDate localDate = LocalDate.parse("2023-07-06");
        LocalDateTime dateTime = localDate.atStartOfDay();
        List<Comment> comments = commentRepository.searchCommentsByDate(dateTime);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions
                .assertThat(comments)
                .hasSize(1);
        Assertions
                .assertThat(comments.get(0)
                        .getText())
                .isEqualTo("I appreciate the efforts made to raise awareness and support for mental health.");
    }
}
