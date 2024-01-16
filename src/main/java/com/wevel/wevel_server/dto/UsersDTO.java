package com.wevel.wevel_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersDTO {
    private String email;
    private String name;
    private String kakaoId;
    private String naverToken;
    private String googleToken;

    // 생성자, 메소드 등을 추가할 수 있습니다.
}
