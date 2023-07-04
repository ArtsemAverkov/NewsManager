package ru.clevertec.NewsManager.common.integration;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverCommentsRequestDto;
import ru.clevertec.NewsManager.common.utill.RequestId;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(ValidParameterResolverCommentsRequestDto.class)
@DisplayName("News Repository Test")
public class NewsPostgresQLRepositoryTest extends TestContainerInitializer{

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private  TestEntityManager testEntityManager;

    /**
     * Test for searching news with a query parameter.
     */
    @Test
    void shouldSearchNewsHasParameterQuery(){
        List<News> list = newsRepository.searchNewsByQuery("E-commerce");
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions
                .assertThat(list)
                .hasSize(1);
        for (News news : list) {
            System.out.println("news = " + news);
          Assertions
                  .assertThat("The Rise of E-commerce and its Impact on Traditional Retailers")
                  .isEqualTo(news.getTitle());
        }
    }

    /**
     * Test for searching news with a date parameter.
     */
    @Test
    void shouldSearchNewsHasParameterDate(){
        LocalDate localDate = LocalDate.parse("2023-06-01");
        LocalDateTime dateTime = localDate.atStartOfDay();
        List<News> list = newsRepository.searchNewsByDate(dateTime);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions.assertThat(list).hasSize(1);
        Assertions.assertThat(list.get(0).getTitle()).isEqualTo("Exploring the Benefits of Renewable Energy Sources");
    }

    /**
     * Test for finding news with comments.
     */
    @Test
    void shouldFindNewsWithComments(){
        News newsWithComments = newsRepository.findNewsWithComments(RequestId.VALUE_1.getValue());
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions.assertThat(RequestId.VALUE_1.getValue())
                .isEqualTo(newsWithComments.getId());
    }
}
