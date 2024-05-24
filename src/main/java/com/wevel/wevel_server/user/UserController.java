package com.wevel.wevel_server.user;

import com.wevel.wevel_server.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private String secretKey = "userId";

    @Autowired
    private UserFindService userFindService;

    @GetMapping("/token")
    public OAuth2AuthenticationToken token(final OAuth2AuthenticationToken token){
        return token;
    }

    @GetMapping("/id")
    public String getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        System.out.println("세션에서 검색된 사용자 ID: " + userId);

        if (userId != null) {
            return "{\"userId\": " + userId + "}";
        } else {
            System.out.println("세션에 사용자 ID가 없습니다");
            return "{\"error\": \"세션에 사용자 ID가 없습니다\"}";
        }
    }



    @GetMapping("/userId")
    public Long getUserIdFromSession(HttpSession session) {
        Object userIdObject = session.getAttribute("userId");

        if (userIdObject != null) {
            return (Long) userIdObject;
        } else {
            return null;
        }
    }

    @GetMapping("/{userId}")
    public UserResponse getUserInfo(@PathVariable Long userId) {
        Optional<User> userOptional = Optional.ofNullable(userFindService.findById(userId));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserResponse(user.getEmail(), user.getName());
        } else {
            // 해당 userId에 대한 사용자를 찾을 수 없을 경우 예외처리 또는 적절한 응답 처리
            return null;
        }
    }


}
