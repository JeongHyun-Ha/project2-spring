package com.prj2.service.board;

import com.prj2.domain.board.Board;
import com.prj2.mapper.board.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class BoardService {

    private final BoardMapper boardMapper;

    public void add(Board board) {
        boardMapper.insert(board);
    }
}
