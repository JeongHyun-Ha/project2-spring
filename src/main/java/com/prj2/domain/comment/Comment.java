package com.prj2.domain.comment;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Comment {
    private Integer Id;
    private Integer boardId;
    private Integer memberId;
    private String comment;
    private LocalDateTime inserted;

    private String nickName;

    public String getInserted() {
        LocalDateTime beforeOneDay = LocalDateTime.now().minusDays(1);
        if (inserted.isBefore(beforeOneDay)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            return inserted.format(formatter);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
            return inserted.format(formatter);
        }
    }
}
