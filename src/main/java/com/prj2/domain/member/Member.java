package com.prj2.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    private Integer id;
    private String email;
    private String password;
    private String nickName;
    private LocalDateTime inserted;
}
