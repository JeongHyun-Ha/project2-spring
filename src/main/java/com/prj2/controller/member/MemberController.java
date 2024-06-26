package com.prj2.controller.member;

import com.prj2.domain.member.Member;
import com.prj2.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody Member member) {
        if (!memberService.validate(member)) {
            return ResponseEntity.badRequest().build();
        }
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
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public List<Member> list() {
        return memberService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Member> info(@PathVariable Integer id,
                                       Authentication authentication) {
        if (!memberService.hasAccess(id, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Member member = memberService.getById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(member);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity remove(@RequestBody Member member,
                                 Authentication authentication) {

        if (memberService.hasAccess(member, authentication)) {
            memberService.remove(member.getId());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity edit(@RequestBody Member member,
                               Authentication authentication) {
        if (!memberService.hasAccessEdit(member, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, Object> result = memberService.edit(member, authentication);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/token")
    public ResponseEntity token(@RequestBody Member member) {
        Map<String, Object> map = memberService.getToken(member);
        if (map == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.ok().body(map);
        }
    }

}
