package com.prj2.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board {

    private Integer id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime inserted;
}
