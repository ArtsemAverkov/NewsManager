package ru.clevertec.NewsManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;
import ru.clevertec.NewsManager.entity.Comment;
import ru.clevertec.NewsManager.service.comment.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CommentRequestDto comment){
        return commentService.create(comment);
    }

    @GetMapping(value= "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comment read(@PathVariable  @Valid Long id) {
        return commentService.read(id);
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable @Valid Long id, @RequestBody @Valid CommentRequestDto comment){
        commentService.update(comment, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Valid Long id){
         commentService.delete(id);
    }

    @GetMapping(value = "/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> searchComments(@RequestParam(required = false) String query,
                                 @RequestParam(required = false) LocalDateTime date) {
        return commentService.searchComments(query, date);
    }

    @GetMapping
    public List<Comment> readAll(@PageableDefault Pageable pageable){
        return commentService.readAll(pageable);
    }

}
