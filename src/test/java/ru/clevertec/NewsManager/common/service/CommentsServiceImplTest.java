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
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverCommentsRequestDto;
import ru.clevertec.NewsManager.common.utill.RequestId;
import ru.clevertec.NewsManager.common.utill.RequestName;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.CommentRepository;
import ru.clevertec.NewsManager.service.comment.CommentApiService;
import ru.clevertec.NewsManager.service.news.NewsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@DisplayName("Comments Service Test")
public class CommentsServiceImplTest {
    @Nested
    @ExtendWith({MockitoExtension.class, ValidParameterResolverCommentsRequestDto.class})
    public class ValidData {

        @InjectMocks
        private CommentApiService commentApiService;

        @Mock
        private NewsService newsService;

        @Mock
        private CommentRepository commentRepository;

        @BeforeEach
        void setUp() {
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken("username", "password");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        @Test
        void shouldReadCommentsWhenCommentsValid(CommentRequestDto commentRequestDto) {
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            when(commentRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.ofNullable(builderCreateComment));
            assertEquals(builderCreateComment, commentApiService.read(RequestId.VALUE_1.getValue()));
            verify(commentRepository, times(1)).findById(RequestId.VALUE_1.getValue());
        }

        @Test
        void shouldCreateCommentsWhenCommentsIsValid(CommentRequestDto commentRequestDto) {
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            when(newsService.read(commentRequestDto.getNewsId())).thenReturn(new News());
            when(commentRepository.save(any(Comment.class))).thenReturn(builderCreateComment);
            assertEquals(RequestId.VALUE_1.getValue(), commentApiService.create(commentRequestDto));
            verify(commentRepository, times(1)).save(any(Comment.class));
        }

        @Test
        void shouldSearchCommentsWhenSearchCommentsHasParameterQuery(CommentRequestDto commentRequestDto) {
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            List<Comment> listComment = new ArrayList<>();
           listComment.add(builderCreateComment);
            when(commentRepository
                    .searchCommentsByQuery(RequestName.NAME.getValue()))
                    .thenReturn(listComment);
            assertEquals(listComment, commentApiService.searchComments(RequestName.NAME.getValue(), null));
            verify(commentRepository, times(1)).searchCommentsByQuery(RequestName.NAME.getValue());
        }

        @Test
        void shouldSearchCommentsWhenSearchCommentsHasParameterData(CommentRequestDto commentRequestDto) {
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            List<Comment> listComment = new ArrayList<>();
            listComment.add(builderCreateComment);
            LocalDateTime now = LocalDateTime.now();
            when(commentRepository
                    .searchCommentsByDate(now))
                    .thenReturn(listComment);
            assertEquals(listComment, commentApiService.searchComments(null, now));
            verify(commentRepository, times(1)).searchCommentsByDate(now);
        }

        @Test
        void shouldReadAllWhenCommentsIsValid(CommentRequestDto commentRequestDto) {
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            List<Comment> listComment = new ArrayList<>();
            listComment.add(builderCreateComment);
            Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
            when(commentRepository.findAll(pageable))
                    .thenReturn(new PageImpl<>(listComment));
            assertEquals(listComment, commentApiService.readAll(Pageable.ofSize(10).withPage(0)));
            verify(commentRepository, times(1)).findAll(Pageable.ofSize(10).withPage(0));
        }

        @Test
        void testUpdateWithValidAuthorName(CommentRequestDto commentRequestDto) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            builderCreateComment.setUsername(authentication.getName());
            when(commentRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(builderCreateComment));
            commentApiService.update(commentRequestDto, RequestId.VALUE_1.getValue());
            verify(commentRepository, times(1)).save(any(Comment.class));
        }

        @Test
        void testUpdateWithInvalidAuthorName(CommentRequestDto commentRequestDto) {
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            builderCreateComment.setUsername("admin");
            when(commentRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of( builderCreateComment));
            assertThrows(AccessDeniedException.class, () -> commentApiService.update(commentRequestDto,  RequestId.VALUE_1.getValue()));
        }

        @Test
        void testDeleteWithValidAuthorName(CommentRequestDto commentRequestDto) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            builderCreateComment.setUsername(authentication.getName());
            when(commentRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of(builderCreateComment));
            commentApiService.delete(RequestId.VALUE_1.getValue());
            verify(commentRepository, times(1)).deleteById(RequestId.VALUE_1.getValue());

        }

        @Test
        void testDeleteWithInvalidAuthorName(CommentRequestDto commentRequestDto) {
            Comment builderCreateComment = builderCreateComment(commentRequestDto);
            builderCreateComment.setUsername("admin");
            when(commentRepository.findById(RequestId.VALUE_1.getValue())).thenReturn(Optional.of( builderCreateComment));
            assertThrows(AccessDeniedException.class, () -> commentApiService.delete(RequestId.VALUE_1.getValue()));
        }


        private Comment builderCreateComment(CommentRequestDto commentRequestDto){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            LocalDateTime now = LocalDateTime.now().withNano(0);
            return Comment.builder()
                    .id(RequestId.VALUE_1.getValue())
                    .time(now)
                    .text(commentRequestDto.getText())
                    .username(authentication.getName())
                    .build();
        }
    }

    @Nested
    @ExtendWith({MockitoExtension.class, ValidParameterResolverCommentsRequestDto.class})
    public class InvalidData {


        @InjectMocks
        private CommentApiService commentApiService;

        @Mock
        private CommentRepository commentRepository;

        @Test
        void shouldGetGiftCertificatesWheGiftCertificatesIsInvalid() {
            when(commentRepository.findById(1L)) .thenReturn(Optional.ofNullable(null));
            assertThrows(IllegalArgumentException.class, () -> commentApiService.read(1L));
        }
    }
}