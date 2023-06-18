package ru.clevertec.NewsManager.common.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverCommentsRequestDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.repository.CommentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    void shouldSearchCommentsHasParameterDate(){
        LocalDate localDate = LocalDate.parse("2023-05-26");
        LocalDateTime dateTime = localDate.atStartOfDay();
        List<Comment> comments = commentRepository.searchCommentsByDate(dateTime);
        System.out.println("comments = " + comments);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions.assertThat(comments).hasSize(1);
        Assertions.assertThat(comments.get(0).getText()).isEqualTo("Sample Comment Text");
    }
}
