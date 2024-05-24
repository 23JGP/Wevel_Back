package com.wevel.wevel_server.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SessionController {

    @GetMapping("/getUserId")
    public Long getUserIdFromSession(HttpSession session) {
        String secretKey = "userId";

        // 동기화를 고려하여 세션에서 userId 값을 가져옴
        synchronized (session) {
            return (Long) session.getAttribute(secretKey);
        }
    }
}