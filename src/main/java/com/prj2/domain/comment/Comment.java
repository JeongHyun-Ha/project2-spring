package com.prj2.domain.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer Id;
    private Integer boardId;
    private Integer memberId;
    private String comment;
    private LocalDateTime inserted;
}
