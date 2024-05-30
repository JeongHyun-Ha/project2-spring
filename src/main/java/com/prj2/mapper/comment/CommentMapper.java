package com.prj2.mapper.comment;

import com.prj2.domain.comment.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
             INSERT INTO comment
            (board_id, member_id, comment)
             VALUES (#{boardId}, #{memberId}, #{comment})
            """)
    int insert(Comment comment);

    @Select("""
            SELECT c.id, c.board_id, c.comment, c.inserted, m.nick_name
            FROM comment c JOIN member m ON c.member_id = m.id
            WHERE board_id=#{boardId}
            ORDER BY id
            """)
    List<Comment> selectAllByBoardId(Integer boardId);
}
