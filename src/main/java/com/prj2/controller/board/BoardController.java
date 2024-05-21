package com.prj2.controller.board;

import com.prj2.domain.board.Board;
import com.prj2.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/add")
    public ResponseEntity<Board> add(@RequestBody Board board) throws InterruptedException {

        // 검증
        if (!boardService.validate(board)) {
            return ResponseEntity.badRequest().build();
        }

        boardService.add(board);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public List<Board> list() {
        return boardService.list();
    }

    @GetMapping("/{id}")
    public Board get(@PathVariable Integer id) {
        return boardService.get(id);
    }
}
