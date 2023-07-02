package ru.clevertec.NewsManager.common.integration;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.NewsManager.common.WireMockInitializer;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverNewsRequestDto;
import ru.clevertec.NewsManager.common.utill.RequestId;
import ru.clevertec.NewsManager.common.utill.RequestName;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.NewsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 Integration tests for the NewsPostgresQLRepository class.
 */

@DataJpaTest
@Testcontainers
@ExtendWith(ValidParameterResolverNewsRequestDto.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("News Repository Test")
public class NewsPostgresQLRepositoryTest extends TestContainerInitializer{

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private  TestEntityManager testEntityManager;

    @BeforeAll
    static void setup() {
        WireMockInitializer.setup();
    }

    @AfterAll
    static void teardown() {
        WireMockInitializer.teardown();
    }

    /**
     * Test for searching news with a query parameter.
     */
    @Sql("/INITIAL_DB_SCRIPT.sql")
    @Test
    void shouldSearchNewsHasParameterQuery(){
        List<News> list = newsRepository.searchNewsByQuery(RequestName.ADMIN.getValue());
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        for (News news : list) {
            Assertions.assertThat(RequestName.ADMIN.getValue()).isEqualTo(news.getAuthor());
        }
    }

    /**
     * Test for searching news with a date parameter.
     */
    @Sql("/INITIAL_DB_SCRIPT.sql")
    @Test
    void shouldSearchNewsHasParameterDate(){
        LocalDate localDate = LocalDate.parse("2023-05-26");
        LocalDateTime dateTime = localDate.atStartOfDay();
        List<News> list = newsRepository.searchNewsByDate(dateTime);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions.assertThat(list).hasSize(3);
        Assertions.assertThat(list.get(0).getTitle()).isEqualTo("Sample News Title");
    }

    /**
     * Test for finding news with comments.
     */
    @Sql("/INITIAL_DB_SCRIPT.sql")
    @Test
    void shouldFindNewsWithComments(){
        News newsWithComments = newsRepository.findNewsWithComments(RequestId.VALUE_1.getValue());
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions.assertThat(RequestId.VALUE_1.getValue())
                .isEqualTo(newsWithComments.getId());
    }
}
