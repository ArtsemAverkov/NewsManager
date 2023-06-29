package ru.clevertec.NewsManager.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.NewsManager.builder.CommentBuilder;
import ru.clevertec.NewsManager.dto.request.CommentRequestProtos;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.CommentRepository;
import ru.clevertec.NewsManager.service.news.NewsService;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 The CommentApiService class implements the CommentService interface and provides
 the functionality to manage comments in the NewsManager application.
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "commentCaches")
public class CommentApiService implements CommentService{

    private final CommentRepository commentRepository;
    private final NewsService newsService;

    /**
     Creates a new comment based on the provided comment request DTO.
     @param comment the comment request DTO
     @return the ID of the created comment
     */

    @CachePut(value = "comment", key = "#id")
    @Transactional
    @Override
    public long create(CommentRequestProtos.CommentRequestDto comment) {
        News news = newsService.read(comment.getNewsId());
        Comment builderComment = CommentBuilder.buildCreateComment(comment);
        builderComment.setNews(news);
        return commentRepository.save(builderComment).getId();
    }

    /**
     Retrieves a comment by its ID.
     @param id the ID of the comment
     @return the retrieved comment
     @throws IllegalArgumentException if the comment ID is invalid
     */

    @Cacheable(cacheNames = "comment", key = "#id", unless = "#result == null")
    @Override
    public Comment read(long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid Comment Id:" + id));
    }

    /**
     Updates a comment with the provided comment request DTO and ID.
     @param comment the comment request DTO
     @param id the ID of the comment to update
     @throws AccessDeniedException if the current user is not the author of the comment
     */

    @CachePut(value = "comment", key = "#id")
    @Override
    @Transactional
    public void update(CommentRequestProtos.CommentRequestDto comment, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment readComment = read(id);
        if (readComment.getUsername().equals(authentication.getName())) {
            LocalDateTime time = readComment.getTime();
            readComment.setId(id);
            Comment builderUpdateComment = CommentBuilder.buildUpdateComment(comment, time);
            commentRepository.save(builderUpdateComment);
        } else {
            throw new AccessDeniedException(
                    "You do not have permission to update the comment as you are not the author of this comment.");
        }
    }

    /**
     Deletes a comment with the provided ID.
     @param id the ID of the comment to delete
     @throws AccessDeniedException if the current user is not the author of the comment
     */

    @CacheEvict(value = "comment", key = "#id")
    @Override
    @Transactional
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment readComment = read(id);
        if (readComment.getUsername().equals(authentication.getName())) {
            commentRepository.deleteById(id);
        } else {
            throw new AccessDeniedException(
                    "You do not have permission to delete this comment because you are not the author of this comment.");
        }
    }

    /**

     Retrieves all comments with pagination.
     @param pageable the pagination information
     @return the list of retrieved comments
     */

    @Cacheable(cacheNames = "commentCaches")
    @Override
    public List<Comment> readAll(Pageable pageable) {
        return  commentRepository.findAll(pageable).getContent();
    }

    /**
     Searches for comments based on the provided query and date.
     @param query the search query
     @param date the search date
     @return the list of matching comments
     */

    @Override
    public List<Comment> searchComments(String query, LocalDateTime date) {
        if(Objects.nonNull(query)){
            return commentRepository.searchCommentsByQuery(query);
        }
        return commentRepository.searchCommentsByDate(date);
    }
}
