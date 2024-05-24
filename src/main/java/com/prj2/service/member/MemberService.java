package com.prj2.service.member;

import com.prj2.domain.member.Member;
import com.prj2.mapper.board.BoardMapper;
import com.prj2.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    final MemberMapper memberMapper;
    final BCryptPasswordEncoder passwordEncoder;
    final JwtEncoder encoder;
    private final BoardMapper boardMapper;

    public void add(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setEmail(member.getEmail().trim());
        member.setNickName(member.getNickName().trim());

        memberMapper.insert(member);
    }

    public Member getByEmail(String email) {
        return memberMapper.selectByEmail(email.trim());
    }

    public Member getByNickName(String nickName) {
        return memberMapper.selectByNickName(nickName.trim());
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
        return memberMapper.selectAll();
    }

    public Member getById(Integer id) {
        return memberMapper.selectById(id);
    }

    public void remove(Integer id) {
        boardMapper.deleteByMemberId(id);
        memberMapper.deleteById(id);
    }

    public void edit(Member member) {
        if (member.getPassword() != null && member.getPassword().length() > 0) {
            log.info("새로운 암호");
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        } else {
            log.info("기존 암호");
            Member dbMember = memberMapper.selectById(member.getId());
            member.setPassword(dbMember.getPassword());
        }
        memberMapper.update(member);
    }

    public boolean hasAccess(Member member, Authentication authentication) {
        if (!member.getId().toString().equals(authentication.getName())) {
            return false;
        }

        Member dbMember = memberMapper.selectById(member.getId());

        if (dbMember == null) {
            return false;
        }

        return passwordEncoder.matches(member.getPassword(), dbMember.getPassword());
    }

    public boolean hasAccessEdit(Member member) {
        Member dbMember = memberMapper.selectById(member.getId());

        if (dbMember == null) {
            return false;
        }

        return passwordEncoder.matches(member.getOldPassword(), dbMember.getPassword());
    }

    public Map<String, Object> getToken(Member member) {
        Map<String, Object> result = null;

        Member dbMember = memberMapper.selectByEmail(member.getEmail());
        if (dbMember != null) {
            if (passwordEncoder.matches(member.getPassword(), dbMember.getPassword())) {
                result = new HashMap<>();
                String token = "";
                Instant now = Instant.now();
                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(60 * 60 * 24 * 7))
                        .subject(dbMember.getId().toString())
                        .claim("scope", "") // 권한
                        .claim("nickName", dbMember.getNickName())
                        .build();

                token = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

                result.put("token", token);
            }
        }

        return result;
    }
}
