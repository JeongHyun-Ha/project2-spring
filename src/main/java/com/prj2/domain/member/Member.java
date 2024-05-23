package com.prj2.domain.member;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class Member {

    private Integer id;
    private String email;
    private String password;
    private String oldPassword;
    private String nickName;
    private LocalDateTime inserted;

    public String getSignupDateAndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");
        return inserted.format(formatter);
    }
}
