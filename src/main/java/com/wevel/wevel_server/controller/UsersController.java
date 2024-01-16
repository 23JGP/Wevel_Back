package com.wevel.wevel_server.controller;
import com.wevel.wevel_server.dto.ApiResponse;
import com.wevel.wevel_server.entity.Users;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.wevel.wevel_server.dto.UsersDTO;
import com.wevel.wevel_server.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UsersDTO usersDTO) {
        try {
            usersService.createUser(usersDTO);
            return new ResponseEntity<>(new ApiResponse("유저 테이블에 추가되었습니다."), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("유저 추가가 안되었습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 다른 API 엔드포인트 및 메소드들을 추가할 수 있습니다.
}