package ru.clevertec.NewsManager.common.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.NewsManager.common.WireMockInitializer;
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
@ExtendWith(ValidParameterResolverCommentsRequestDto.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Comments Repository Test")
public class CommentsPostgresQLRepositoryTest extends TestContainerInitializer{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeAll
    static void setup() {
        WireMockInitializer.setup();
    }

    @AfterAll
    static void teardown() {
        WireMockInitializer.teardown();
    }

    /**
     * Test for searching comments with a query parameter.
     */
    @Sql("/INITIAL_DB_SCRIPT.sql")
    @Test
    void shouldSearchCommentsHasParameterQuery(){
        String query = "Comment";
        List<Comment> comments = commentRepository.searchCommentsByQuery(query);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        for (Comment comment : comments) {
            Assertions.assertThat("Sample Comment Text").isEqualTo(comment.getText());
        }
    }

    /**
     * Test for searching comments with a date parameter.
     */
    @Sql("/INITIAL_DB_SCRIPT.sql")
    @Test
    void shouldSearchCommentsHasParameterDate(){
        LocalDate localDate = LocalDate.parse("2023-05-26");
        LocalDateTime dateTime = localDate.atStartOfDay();
        List<Comment> comments = commentRepository.searchCommentsByDate(dateTime);
        System.out.println("comments = " + comments);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions.assertThat(comments).hasSize(2);
        Assertions.assertThat(comments.get(0).getText()).isEqualTo("Sample Comment Text");
    }
}
