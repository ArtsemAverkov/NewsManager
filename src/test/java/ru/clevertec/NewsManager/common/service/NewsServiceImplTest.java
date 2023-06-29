package ru.clevertec.NewsManager.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverCommentsRequestDto;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverNewsRequestDto;
import ru.clevertec.NewsManager.common.utill.RequestId;
import ru.clevertec.NewsManager.common.utill.RequestName;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;
import ru.clevertec.NewsManager.dto.response.NewsResponseDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.NewsRepository;
import ru.clevertec.NewsManager.service.news.NewsApiService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static ru.clevertec.NewsManager.common.utill.NewsBuilder.*;

/**
 * This class contains unit tests for the {@link NewsApiService} class in the package ru.clevertec.NewsManager.common.service.
 */
@DisplayName("News Service Test")
public class NewsServiceImplTest {

    /**
     * Nested class for valid data test cases.
     */
    @Nested
    @ExtendWith({MockitoExtension.class, ValidParameterResolverNewsRequestDto.class, ValidParameterResolverCommentsRequestDto.class})
    public class ValidData {

        @InjectMocks
        private NewsApiService newsApiService;

        @Mock
        private NewsRepository newsRepository;

        /**
         * Sets up the necessary authentication before each test.
         */
        @BeforeEach
        void setUp() {
            Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        /**
         * Test case for reading news when the news ID is valid.
         * @param news the news request DTO
         */
        @Test
        void shouldReadNewsWhenNewsValid(NewsRequestDto news) {
            News newsCreate = buildCreateNews(news);
            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.ofNullable(newsCreate));
            assertEquals(newsCreate, newsApiService.read(RequestId.VALUE_1.getValue()));
            verify(newsRepository, times(1)).findById(RequestId.VALUE_1.getValue());
        }

        /**
         * Test case for creating a news when the news data is valid.
         * @param news the news request DTO
         */
        @Test
        void shouldCreateUserWhenUserIsValid(NewsRequestDto news) {
            News newsCreate = buildCreateNews(news);
            when(newsRepository.save(any(News.class))).thenReturn(newsCreate);
            assertEquals(RequestId.VALUE_1.getValue(), newsApiService.create(news));
        }

        /**
         * Test case for reading news with comments when the news ID is valid and readNewsWithCommentsIsActive is true.
         * @param news              the news request DTO
         * @param commentRequestDto the comment request DTO
         */
        @Test
        void shouldReadNewsWithCommentsWhenReadNewsWithCommentsIsActive(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            News newsCreate = getNews(news, commentRequestDto);
            NewsResponseDto newsResponseDto = buildResponse(newsCreate);
            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(newsCreate));
            when(newsRepository.findNewsWithComments(RequestId.VALUE_1.getValue())).thenReturn(newsCreate);

            assertEquals(newsResponseDto, newsApiService.readNewsWithComments(RequestId.VALUE_1.getValue()));
            verify(newsRepository, times(1)).findNewsWithComments(RequestId.VALUE_1.getValue());
        }

        /**
         * Test case for searching news by query when the query parameter is provided.
         * @param news              the news request DTO
         * @param commentRequestDto the comment request DTO
         */
        @Test
        void shouldSearchNewsWhenSearchNewsHasParameterQuery(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            News newsCreate = getNews(news, commentRequestDto);
            List<News> listNews = new ArrayList<>();
            listNews.add(newsCreate);
            List<NewsResponseDto> newsResponseDtos = buildResponseList(listNews);

            when(newsRepository.searchNewsByQuery(RequestName.NAME.getValue()))
                    .thenReturn(listNews);
            assertEquals(newsResponseDtos, newsApiService.searchNews(RequestName.NAME.getValue(),null));
            verify(newsRepository, times(1)).searchNewsByQuery(RequestName.NAME.getValue());
        }

        /**
         * Test case for searching news by date when the date parameter is provided.
         * @param news              the news request DTO
         * @param commentRequestDto the comment request DTO
         */
        @Test
        void shouldSearchNewsWhenSearchNewsHasParameterDate(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            News newsCreate = getNews(news, commentRequestDto);
            List<News> listNews = new ArrayList<>();
            listNews.add(newsCreate);

            List<NewsResponseDto> newsResponseDtos = buildResponseList(listNews);
            LocalDateTime now = LocalDateTime.now();

            when(newsRepository.searchNewsByDate(now))
                    .thenReturn(listNews);
            assertEquals(newsResponseDtos, newsApiService.searchNews(null, now));
            verify(newsRepository, times(1)).searchNewsByDate(now);
        }

        /**
         * Test case for reading all news when the user is valid.
         * @param news              the news request DTO
         * @param commentRequestDto the comment request DTO
         */
        @Test
        void shouldReadAllWhenUserIsValid(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            News newsCreate = getNews(news, commentRequestDto);
            List<News> listNews = new ArrayList<>();
            listNews.add(newsCreate);

            List<NewsResponseDto> newsResponseDtos = buildResponseList(listNews);
            Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

            when(newsRepository.findAll(pageable))
                    .thenReturn(new PageImpl<>(listNews));
            assertEquals(newsResponseDtos, newsApiService.readAll(Pageable.ofSize(10).withPage(0)));
            verify(newsRepository, times(1)).findAll(Pageable.ofSize(10).withPage(0));
        }

        /**
         * Test case for updating a news with a valid author name.
         * @param news              the news request DTO
         * @param commentRequestDto the comment request DTO
         */
        @Test
        void testUpdateWithValidAuthorName(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<Comment> commentList = getComments(commentRequestDto);
            News newsCreate = buildCreateNews(news);
            newsCreate.setAuthor(authentication.getName());
            newsCreate.setComment(commentList);

            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(newsCreate));
            newsApiService.update(news, RequestId.VALUE_1.getValue());
            verify(newsRepository, times(1)).save(any(News.class));
        }

        /**
         * Test case for updating a news with an invalid author name.
         * @param news              the news request DTO
         * @param commentRequestDto the comment request DTO
         */
        @Test
        void testUpdateWithInvalidAuthorName(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            List<Comment> commentList = getComments(commentRequestDto);
            News newsCreate = buildCreateNews(news);
            newsCreate.setAuthor(RequestName.ADMIN.getValue());
            newsCreate.setComment(commentList);

            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(newsCreate));
            assertThrows(AccessDeniedException.class, () -> newsApiService.update(news,  RequestId.VALUE_1.getValue()));
        }

        /**
         * Test case for deleting a news with a valid author name.
         * @param news              the news request DTO
         * @param commentRequestDto the comment request DTO
         */
        @Test
        void testDeleteWithValidAuthorName(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<Comment> commentList = getComments(commentRequestDto);
            News newsCreate = buildCreateNews(news);
            newsCreate.setAuthor(authentication.getName());
            newsCreate.setComment(commentList);

            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(newsCreate));
            newsApiService.delete(RequestId.VALUE_1.getValue());
            verify(newsRepository, times(1)).deleteById(RequestId.VALUE_1.getValue());

        }

        /**
         * Test case for deleting a news with an invalid author name.
         * @param news              the news request DTO
         * @param commentRequestDto the comment request DTO
         */
        @Test
        void testDeleteWithInvalidAuthorName(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            List<Comment> commentList = getComments(commentRequestDto);
            News newsCreate = buildCreateNews(news);
            newsCreate.setAuthor(RequestName.ADMIN.getValue());
            newsCreate.setComment(commentList);

            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(newsCreate));
            assertThrows(AccessDeniedException.class, () -> newsApiService.delete(RequestId.VALUE_1.getValue()));
        }
    }

    @Nested
    @ExtendWith({MockitoExtension.class,  ValidParameterResolverNewsRequestDto.class})
    public class InvalidData {


        @InjectMocks
        private NewsApiService newsApiService;

        @Mock
        private NewsRepository newsRepository;

        /**
         * Test case for getting news when the news is invalid.
         */
        @Test
        void shouldGetGiftCertificatesWheGiftCertificatesIsInvalid() {
            when(newsRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
            assertThrows(IllegalArgumentException.class, () -> newsApiService.read(1L));
        }
    }
}


