package com.prj2.service.member;

import com.prj2.domain.member.Member;
import com.prj2.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    final MemberMapper mapper;
    final BCryptPasswordEncoder passwordEncoder;

    public void add(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setEmail(member.getEmail().trim());
        member.setNickName(member.getNickName().trim());

        mapper.insert(member);
    }

    public Member getByEmail(String email) {
        return mapper.selectByEmail(email.trim());
    }

    public Member getByNickName(String nickName) {
        return mapper.selectByNickName(nickName.trim());
    }

    public boolean validate(Member member) {
        if (member.getEmail() == null || member.getEmail().isBlank()) {
            return false;
        }

        if (member.getNickName() == null || member.getNickName().isBlank()) {
            return false;
        }

        if (member.getPassword() == null || member.getPassword().isBlank()) {
            return false;
        }

        String emailPattern = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*";

        if (!member.getEmail().trim().matches(emailPattern)) {
            return false;
        }

        return true;
    }

    public List<Member> list() {
        return mapper.selectAll();
    }

    public Member getById(Integer id) {
        return mapper.selectById(id);
    }

    public void remove(Integer id) {
        mapper.deleteById(id);
    }

    public void edit(Member member) {
        if (member.getPassword() != null && member.getPassword().length() > 0) {
            log.info("새로운 암호");
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        } else {
            log.info("기존 암호");
            Member dbMember = mapper.selectById(member.getId());
            member.setPassword(dbMember.getPassword());
        }
        mapper.update(member);
    }

    public boolean hasAccess(Member member) {
        Member dbMember = mapper.selectById(member.getId());

        if (dbMember == null) {
            return false;
        }

        return passwordEncoder.matches(member.getPassword(), dbMember.getPassword());
    }

    public boolean hasAccessEdit(Member member) {
        Member dbMember = mapper.selectById(member.getId());

        if (dbMember == null) {
            return false;
        }

        return passwordEncoder.matches(member.getOldPassword(), dbMember.getPassword());
    }
}
