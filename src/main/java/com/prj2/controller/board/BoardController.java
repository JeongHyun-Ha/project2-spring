package com.prj2.controller.board;

import com.prj2.domain.board.Board;
import com.prj2.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity add(Board board,
                              @RequestParam(value = "files[]", required = false) MultipartFile[] files,
                              Authentication authentication) throws IOException {

        // 검증
        if (!boardService.validate(board)) {
            return ResponseEntity.badRequest().build();
        }

        boardService.add(board, files, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(value = "type", required = false) String searchType,
                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {


        return boardService.list(page, searchType, keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Integer id,
                              Authentication authentication) {
        Map<String, Object> result = boardService.get(id, authentication);

        if (result.get("board") == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity edit(Board board,
                               @RequestParam(value = "removeFileList[]", required = false) List<String> removeFileList,
                               @RequestParam(value = "addFileList[]", required = false) MultipartFile[] addFileList,
                               Authentication authentication) throws IOException {

        if (!boardService.hasAccess(board.getId(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!boardService.validate(board)) {
            return ResponseEntity.badRequest().build();
        }

        boardService.edit(board, removeFileList, addFileList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity delete(@PathVariable Integer id,
                                 Authentication authentication) {

        if (boardService.hasAccess(id, authentication)) {
            boardService.remove(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/like")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> like(@RequestBody Map<String, Object> req,
                                    Authentication authentication) {
        return boardService.like(req, authentication);
    }

}
