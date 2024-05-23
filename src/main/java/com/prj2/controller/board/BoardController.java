package com.prj2.controller.board;

import com.prj2.domain.board.Board;
import com.prj2.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Board> add(@RequestBody Board board,
                                     Authentication authentication) {

        // 검증
        if (!boardService.validate(board)) {
            return ResponseEntity.badRequest().build();
        }

        boardService.add(board, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public List<Board> list() {
        return boardService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Integer id) {
        Board board = boardService.get(id);

        if (board == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(board);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        boardService.remove(id);
    }

    @PutMapping("/edit")
    public ResponseEntity edit(@RequestBody Board board,
                               Authentication authentication) {
        if (!boardService.validate(board)) {
            return ResponseEntity.badRequest().build();
        }
        boardService.edit(board);
        return ResponseEntity.ok().build();
    }
}
