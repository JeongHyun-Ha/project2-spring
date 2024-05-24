package com.prj2.mapper.member;

import com.prj2.domain.member.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    @Insert("""
            INSERT INTO member (email, password, nick_name)
            VALUES (#{email}, #{password}, #{nickName})
            """)
    void insert(Member member);

    @Select("SELECT * FROM member WHERE email=#{email}")
    Member selectByEmail(String email);

    @Select("SELECT * FROM member WHERE nick_name=#{nickName}")
    Member selectByNickName(String nickName);

    @Select("SELECT * FROM member ORDER BY id DESC ")
    List<Member> selectAll();

    @Select("""
            SELECT *
            FROM member WHERE id=#{id}
            """)
    Member selectById(Integer id);

    @Update("""
            UPDATE member
            SET password=#{password}, nick_name=#{nickName}
            WHERE id = #{id}
            """)
    void update(Member member);

    @Delete("DELETE FROM member WHERE id=#{id}")
    void deleteById(Integer id);

    @Select("""
            SELECT *
            FROM authority
            WHERE member_id=#{memberId}
            """)
    List<String> selectAuthorityByMemberId(Integer memberId);
}
