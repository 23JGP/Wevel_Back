package com.wevel.wevel_server.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("/token")
    public OAuth2AuthenticationToken token(final OAuth2AuthenticationToken token){
        return token;
    }

    @GetMapping("/id")
    public String getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            System.out.println("세션에서 검색된 사용자 ID: " + userId);
            return "{\"userId\": " + userId + "}";
        } else {
            System.out.println("세션에 사용자 ID가 없습니다");
            return "{\"error\": \"세션에 사용자 ID가 없습니다\"}";
        }
    }
}
