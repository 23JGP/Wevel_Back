package com.wevel.wevel_server.controller;
import com.wevel.wevel_server.dto.ApiResponse;
import com.wevel.wevel_server.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.wevel.wevel_server.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final HttpSession httpSession;

    public UsersController(UsersService usersService, HttpSession httpSession) {
        this.usersService = usersService;
        this.httpSession = httpSession;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody SessionUser usersDTO) {
        try {
            usersService.createUser(usersDTO);
            return new ResponseEntity<>(new ApiResponse("유저 테이블에 추가되었습니다."), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("유저 추가가 안되었습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/token")
    public OAuth2AuthenticationToken home(final OAuth2AuthenticationToken authentication) {
        return authentication;
    }

    @GetMapping("/")
    public String index(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "naver";
    }
}