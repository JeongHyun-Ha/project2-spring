package com.prj2.controller.comment;

import com.prj2.domain.comment.Comment;
import com.prj2.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity addComment(@RequestBody Comment comment,
                                     Authentication authentication) {

        if (!commentService.validate(comment)) {
            return ResponseEntity.badRequest().build();
        }

        commentService.add(comment, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{boardId}")
    public List<Comment> list(@PathVariable Integer boardId) {

        return commentService.list(boardId);
    }

    @DeleteMapping("/remove")
    public ResponseEntity remove(@RequestBody Comment comment, Authentication authentication) {

        if (!commentService.hasAccess(comment, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentService.remove(comment);
        return ResponseEntity.ok().build();
    }
}
