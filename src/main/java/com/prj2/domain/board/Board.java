package com.prj2.domain.board;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile[] files;

    private Integer numberOfImages;
    private List<String> imageSrcList;
}
