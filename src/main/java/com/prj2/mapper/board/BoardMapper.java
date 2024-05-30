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
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Board board);

    @Select("""
            SELECT b.id,
                   b.title,
                   m.nick_name writer,
                   b.inserted
            FROM board b JOIN member m ON b.member_id = m.id
            ORDER BY id DESC
            """)
    List<Board> selectAll();

    @Select("""
            SELECT b.id,
                   b.title,
                   b.content,
                   m.nick_name writer,
                   b.inserted,
                   b.member_id
            FROM board b JOIN member m ON b.member_id = m.id
            WHERE b.id=#{id}
            """)
    Board selectById(Integer id);

    @Update("""
            UPDATE board
            SET title=#{title}, content=#{content}
            WHERE id=#{id}
            """)
    int update(Board board);

    @Delete("DELETE FROM board WHERE id=#{id}")
    int deleteById(Integer id);

    @Delete("""
            DELETE FROM board
            WHERE member_id=#{memberId}
            """)
    void deleteByMemberId(Integer memberId);

    @Select("SELECT COUNT(*) FROM board ")
    Integer countAll();

    @Select("""
            <script>
            SELECT b.id,
                   b.title,
                   m.nick_name writer,
                   b.inserted,
                   COUNT(DISTINCT f.name) number_of_images,
                   COUNT(DISTINCT l.member_id) number_of_like
            FROM board b JOIN member m ON b.member_id = m.id
                         LEFT JOIN board_file f ON b.id = f.board_id
                         LEFT JOIN board_like l ON b.id = l.board_id
               <trim prefix="WHERE" prefixOverrides="OR">
                   <if test="searchType != null">
                       <bind name="pattern" value="'%' + keyword + '%'" />
                       <if test="searchType == 'all' || searchType == 'text'">
                           OR b.title LIKE #{pattern}
                           OR b.content LIKE #{pattern}
                       </if>
                       <if test="searchType == 'all' || searchType == 'nickName'">
                           OR m.nick_name LIKE #{pattern}
                       </if>
                   </if>
               </trim>
            GROUP BY b.id
            ORDER BY b.id DESC
            LIMIT #{offset}, 10
            </script>
            """)
    List<Board> selectAllPaging(Integer offset, String searchType, String keyword);

    @Select("""
            <script>
            SELECT COUNT(b.id)
            FROM board b JOIN member m ON b.member_id = m.id
               <trim prefix="WHERE" prefixOverrides="OR">
                   <if test="searchType != null">
                       <bind name="pattern" value="'%' + keyword + '%'" />
                       <if test="searchType == 'all' || searchType == 'text'">
                           OR b.title LIKE #{pattern}
                           OR b.content LIKE #{pattern}
                       </if>
                       <if test="searchType == 'all' || searchType == 'nickName'">
                           OR m.nick_name LIKE #{pattern}
                       </if>
                   </if>
               </trim>
            </script>
            """)
    Integer countAllWithSearch(String searchType, String keyword);

    @Insert("""
            INSERT INTO board_file (board_id, name)
            VALUES (#{boardId}, #{name})
            """)
    int insertFileName(Integer boardId, String name);

    @Select("""
            SELECT name
            FROM board_file
            WHERE board_id=#{boardId}
            """)
    List<String> selectFileNameByBoardId(Integer boardId);

    @Select("""
            SELECT id
            FROM board
            WHERE member_id=#{memberId}
            """)
    List<Board> selectByMemberId(Integer memberId);

    @Insert("""
            INSERT INTO board_like (board_id, member_id)
            VALUES (#{boardId}, #{memberId})
            """)
    void insertLikeByBoardIdAndMemberId(Integer boardId, Integer memberId);

    @Select("""
            SELECT COUNT(*)
            FROM board_like
            WHERE board_id=#{boardId}
            """)
    int selectCountLikeByBoardId(Integer boardId);

    @Select("""
            SELECT COUNT(*)
            FROM board_like
            WHERE board_id=#{boardId}
              AND member_id=#{memberId}
            """)
    int selectLikeByBoardIdAndMemberId(Integer boardId, String memberId);

    @Delete("""
            DELETE FROM board_file
            WHERE board_id=#{boardId}
            """)
    int deleteFileByBoardId(Integer boardId);

    @Delete("""
            DELETE FROM board_file
            WHERE board_id=#{boardId}
              AND name=#{fileName}
            """)
    int deleteFileByBoardIdAndName(Integer boardId, String fileName);

    @Delete("""
            DELETE FROM board_like
            WHERE board_id=#{boardId}
              AND member_id=#{memberId}
            """)
    int deleteLikeByBoardIdAndMemberId(Integer boardId, Integer memberId);

    @Delete("""
            DELETE FROM board_like
            WHERE board_id=#{boardId}
            """)
    void deleteLikeByBoardId(Integer boardId);
}
