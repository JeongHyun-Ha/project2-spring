package com.prj2.domain.board;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Board {

    private Integer id;
    private String title;
    private String content;
    private String writer;
    private Integer memberId;
    private LocalDateTime inserted;

    private Integer numberOfLike;
    private Integer numberOfImages;
    private Integer numberOfComments;
    private List<BoardFile> fileList;
}
