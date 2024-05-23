package com.prj2.service.board;

import com.prj2.domain.board.Board;
import com.prj2.domain.member.Member;
import com.prj2.mapper.board.BoardMapper;
import com.prj2.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class BoardService {

    private final BoardMapper boardMapper;
    private final MemberMapper memberMapper;

    public void add(Board board, Authentication authentication) {
        log.info("authentication={}", authentication);
        Member member = memberMapper.selectByEmail(authentication.getName());
        log.info("member={}", member);
        board.setMemberId(member.getId());
        boardMapper.insert(board);
    }

    public boolean validate(Board board) {
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }

        return true;
    }

    public List<Board> list() {
        return boardMapper.selectAll();
    }

    public Board get(Integer id) {
        return boardMapper.selectById(id);
    }

    public void remove(Integer id) {
        boardMapper.deleteById(id);
    }

    public void edit(Board board) {
        boardMapper.update(board);
    }
}
