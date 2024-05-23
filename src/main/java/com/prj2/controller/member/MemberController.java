package com.prj2.controller.member;

import com.prj2.domain.member.Member;
import com.prj2.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody Member member) {
        if (memberService.validate(member)) {
            return ResponseEntity.badRequest().build();
        }
        log.info("member={}", member);
        memberService.add(member);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/check", params = "email")
    public ResponseEntity checkEmail(@RequestParam String email) {
        Member member = memberService.getByEmail(email);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/check", params = "nickName")
    public ResponseEntity checkNickName(@RequestParam String nickName) {
        Member member = memberService.getByNickName(nickName);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public List<Member> list() {
        return memberService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> info(@PathVariable Integer id) {

        Member member = memberService.getById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(member);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@RequestBody Member member) {
        if (memberService.hasAccess(member)) {
            memberService.remove(member.getId());
            return ResponseEntity.ok().build();
        }

        // todo : forbidden으로 수정
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/edit")
    public ResponseEntity<Member> edit(@RequestBody Member member) {
        if (!memberService.hasAccessEdit(member)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        memberService.edit(member);
        return ResponseEntity.ok().build();
    }

}
