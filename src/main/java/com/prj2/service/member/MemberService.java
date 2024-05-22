package com.prj2.service.member;

import com.prj2.domain.member.Member;
import com.prj2.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;

    public void add(Member member) {
        mapper.insert(member);
    }

    public Member getByEmail(String email) {
        return mapper.selectByEmail(email);
    }

    public Member getByNickName(String nickName) {
        return mapper.selectByNickName(nickName);
    }
}
