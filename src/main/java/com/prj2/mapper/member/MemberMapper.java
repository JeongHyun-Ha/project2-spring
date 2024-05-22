package com.prj2.mapper.member;

import com.prj2.domain.member.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
