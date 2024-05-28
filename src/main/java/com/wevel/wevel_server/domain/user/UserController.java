package com.wevel.wevel_server.domain.user;

import com.wevel.wevel_server.domain.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {


    @Autowired
    private UserFindService userFindService;

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

