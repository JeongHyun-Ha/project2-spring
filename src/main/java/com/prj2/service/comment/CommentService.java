package com.prj2.service.comment;

import com.prj2.domain.comment.Comment;
import com.prj2.mapper.comment.CommentMapper;
import com.prj2.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommentService {

    private final CommentMapper commentMapper;
    private final MemberMapper memberMapper;

    public void add(Comment comment, Authentication authentication) {
        comment.setMemberId(Integer.valueOf(authentication.getName()));

        commentMapper.insert(comment);
    }

    public List<Comment> list(Integer boardId) {
        return commentMapper.selectAllByBoardId(boardId);
    }

    public boolean validate(Comment comment) {
        if (comment == null) {
            return false;
        }
        if (comment.getComment().isBlank()) {
            return false;
        }
        if (comment.getBoardId() == null) {
            return false;
        }
        return true;
    }

    public void remove(Comment comment) {
        commentMapper.deleteById(comment.getId());
    }

    public boolean hasAccess(Comment comment, Authentication authentication) {
        Comment db = commentMapper.selectById(comment.getId());
        if (db == null) {
            return false;
        }
        if (!authentication.getName().equals(db.getMemberId().toString())) {
            return false;
        }
        return true;
    }
}
