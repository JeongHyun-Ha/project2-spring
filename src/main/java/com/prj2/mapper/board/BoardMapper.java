package com.prj2.mapper.board;

import com.prj2.domain.board.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("""
            INSERT INTO board (title, content, member_id)
            VALUES (#{title}, #{content}, #{memberId})
            """)
    int insert(Board board);

    @Select("SELECT * FROM board ORDER BY id DESC ")
    List<Board> selectAll();

    @Select("SELECT * FROM board WHERE id=#{id}")
    Board selectById(Integer id);

    @Delete("DELETE FROM board WHERE id=#{id}")
    int deleteById(Integer id);

    @Update("""
            UPDATE board
            SET title=#{title}, content=#{content}, member_id=#{memberId}
            WHERE id=#{id}
            """)
    void update(Board board);
}
