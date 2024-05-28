package com.prj2.service.board;

import com.prj2.domain.board.Board;
import com.prj2.mapper.board.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class BoardService {

    private final BoardMapper boardMapper;

    public void add(Board board, MultipartFile[] files, Authentication authentication) {
        board.setMemberId(Integer.valueOf(authentication.getName()));
        boardMapper.insert(board);

        // db에 해당 게시물의 파일 목록 저장
        if (files != null) {
            for (MultipartFile file : files) {
                boardMapper.insertFileName(board.getId(), file.getOriginalFilename());
            }
        }
        // 실제 파일 저장

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

    public Map<String, Object> list(Integer page, String searchType, String keyword) {
        Map<String, Object> pageInfo = new HashMap<>();
        Integer countAll = boardMapper.countAllWithSearch(searchType, keyword);

        Integer offset = (page - 1) * 10;
        Integer lastPageNumber = (countAll - 1) / 10 + 1;
        Integer leftPageNumber = (page - 1) / 10 * 10 + 1;
        Integer rightPageNumber = leftPageNumber + 9;
        rightPageNumber = Math.min(rightPageNumber, lastPageNumber);
        leftPageNumber = rightPageNumber - 9;
        leftPageNumber = Math.max(leftPageNumber, 1);

        Integer prevPageNumber = leftPageNumber - 1;
        Integer nextPageNumber = rightPageNumber + 1;

        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("lastPageNumber", lastPageNumber);
        pageInfo.put("leftPageNumber", leftPageNumber);
        pageInfo.put("rightPageNumber", rightPageNumber);

        return Map.of("pageInfo", pageInfo, "boardList", boardMapper.selectAllPaging(offset, searchType, keyword));
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

    public boolean hasAccess(Integer id, Authentication authentication) {
        Board board = boardMapper.selectById(id);

        return board.getMemberId().equals(Integer.valueOf(authentication.getName()));
    }
}
