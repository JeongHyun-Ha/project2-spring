package com.prj2.controller.comment;

import com.prj2.domain.comment.Comment;
import com.prj2.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public void addComment(@RequestBody Comment comment,
                           Authentication authentication) {

        commentService.add(comment, authentication);
        log.info("comment={}", comment);
    }
}
