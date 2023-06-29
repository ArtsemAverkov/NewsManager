package ru.clevertec.NewsManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.NewsManager.dto.request.CommentRequestProtos;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.service.comment.CommentService;

import java.time.LocalDateTime;
import java.util.List;

/**
 This class serves as a controller for Comment-related operations.
 */

@RestController
@RequestMapping(value = "/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Creates a new comment.
     * @param comment the CommentRequestDto containing the comment details
     * @return the ID of the created comment
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CommentRequestProtos.CommentRequestDto comment){
        return commentService.create(comment);
    }

    /**
     * Retrieves a comment by its ID.
     * @param id the ID of the comment
     * @return the Comment object
     */

    @Transactional
    @GetMapping(value= "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comment read(@PathVariable  @Valid Long id) {
        return commentService.read(id);
    }


    /**
     * Updates a comment.
     * @param id      the ID of the comment to update
     * @param comment the CommentRequestDto containing the updated comment details
     */

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable @Valid Long id, @RequestBody @Valid CommentRequestProtos.CommentRequestDto comment){
        commentService.update(comment, id);
    }

    /**
     * Deletes a comment by its ID.
     * @param id the ID of the comment to delete
     */

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Valid Long id){
         commentService.delete(id);
    }

    /**
     * Searches for comments based on the provided query and date.
     * @param query the search query
     * @param date  the date to filter the comments
     * @return the list of matching comments
     */

    @GetMapping(value = "/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> searchComments(@RequestParam(required = false) String query,
                                 @RequestParam(required = false) LocalDateTime date) {
        return commentService.searchComments(query, date);
    }

    /**
     * Retrieves all comments with pagination support.
     * @param pageable the Pageable object for pagination
     * @return the list of comments
     */

    @GetMapping
    public List<Comment> readAll(@PageableDefault Pageable pageable){
        return commentService.readAll(pageable);
    }

}
