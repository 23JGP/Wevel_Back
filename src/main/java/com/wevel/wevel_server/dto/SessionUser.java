package com.wevel.wevel_server.dto;

import com.wevel.wevel_server.entity.Users;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SessionUser implements Serializable {
// 인증된 사용자 정보들만 담을 수 있는 클래스
//Serializable를 추가해 직렬화 기능을 가짐x
    SessionUser() {}

    public SessionUser(Users user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }

    private String email;
    private String name;
    private String kakaoId;
    private String naverToken;
    private String googleToken;

    // 생성자, 메소드 등을 추가할 수 있습니다.
}
