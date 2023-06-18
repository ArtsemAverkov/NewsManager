package ru.clevertec.NewsManager.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.clevertec.NewsManager.aop.cache.Cacheable;
import ru.clevertec.NewsManager.builder.CommentBuilder;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.entity.News;
import ru.clevertec.NewsManager.repository.CommentRepository;
import ru.clevertec.NewsManager.service.news.NewsService;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentApiService implements CommentService{

    private final CommentRepository commentRepository;
    private final NewsService newsService;

    @Cacheable("myCache")
    @Override
    public long create(CommentRequestDto comment) {
        News news = newsService.read(comment.getNewsId());
        Comment builderComment = CommentBuilder.buildCreateComment(comment);
        builderComment.setNews(news);
        return commentRepository.save(builderComment).getId();
    }

    @Cacheable("myCache")
    @Override
    public Comment read(long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid Comment Id:" + id));
    }

    @Cacheable("myCache")
    @Override
    public void update(CommentRequestDto comment, Long id) {
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

    @Cacheable("myCache")
    @Override
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

    @Override
    public List<Comment> readAll(Pageable pageable) {
        return  commentRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Comment> searchComments(String query, LocalDateTime date) {
        if(Objects.nonNull(query)){
            return commentRepository.searchCommentsByQuery(query);
        }
        return commentRepository.searchCommentsByDate(date);
    }
}
