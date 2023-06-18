package ru.clevertec.NewsManager.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
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
import ru.clevertec.NewsManager.dto.responseNews.NewsResponseDto;
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
import static org.mockito.Mockito.*;
import static ru.clevertec.NewsManager.common.utill.NewsBuilder.*;

@DisplayName("News Service Test")
public class NewsServiceImplTest {
    @Nested
    @ExtendWith({MockitoExtension.class, ValidParameterResolverNewsRequestDto.class, ValidParameterResolverCommentsRequestDto.class})
    public class ValidData {

        @InjectMocks
        private NewsApiService newsApiService;

        @Mock
        private NewsRepository newsRepository;

        @BeforeEach
        void setUp() {
            Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        @Test
        void shouldReadNewsWhenNewsValid(NewsRequestDto news) {
            News newsCreate = buildCreateNews(news);
            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.ofNullable(newsCreate));
            assertEquals(newsCreate, newsApiService.read(RequestId.VALUE_1.getValue()));
            verify(newsRepository, times(1)).findById(RequestId.VALUE_1.getValue());
        }

        @Test
        void shouldCreateUserWhenUserIsValid(NewsRequestDto news) {
            News newsCreate = buildCreateNews(news);
            when(newsRepository.save(any(News.class))).thenReturn(newsCreate);
            assertEquals(RequestId.VALUE_1.getValue(), newsApiService.create(news));
        }

        @Test
        void shouldReadNewsWithCommentsWhenReadNewsWithCommentsIsActive(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            News newsCreate = getNews(news, commentRequestDto);
            NewsResponseDto newsResponseDto = buildResponse(newsCreate);
            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(newsCreate));
            when(newsRepository.findNewsWithComments(RequestId.VALUE_1.getValue())).thenReturn(newsCreate);

            assertEquals(newsResponseDto, newsApiService.readNewsWithComments(RequestId.VALUE_1.getValue()));
            verify(newsRepository, times(1)).findNewsWithComments(RequestId.VALUE_1.getValue());
        }


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

        @Test
        void testUpdateWithInvalidAuthorName(NewsRequestDto news, CommentRequestDto commentRequestDto) {
            List<Comment> commentList = getComments(commentRequestDto);
            News newsCreate = buildCreateNews(news);
            newsCreate.setAuthor(RequestName.ADMIN.getValue());
            newsCreate.setComment(commentList);

            when(newsRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(newsCreate));
            assertThrows(AccessDeniedException.class, () -> newsApiService.update(news,  RequestId.VALUE_1.getValue()));
        }

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

        @Test
        void shouldGetGiftCertificatesWheGiftCertificatesIsInvalid() {
            when(newsRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
            assertThrows(IllegalArgumentException.class, () -> newsApiService.read(1L));
        }
    }
}


