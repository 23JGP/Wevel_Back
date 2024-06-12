package com.wevel.wevel_server.domain.user.service;

import com.wevel.wevel_server.domain.user.entity.User;
import com.wevel.wevel_server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    public void registerUser(String name, String email, String socialId, String provider) {
        User existingUser = userFindService.findBySocialId(socialId);

        if (existingUser != null) {
            // 이미 존재하는 사용자에 대한 처리
            // 여기서는 예외를 throw하여 사용자 등록에 실패했음을 알림
            throw new IllegalStateException("User with this User already exists.");
        } else {
            // 사용자가 존재하지 않는 경우에만 새로운 사용자를 생성하고 저장
            User newUser = new User(name, email, socialId, provider);
            System.out.println(newUser);
            userRepository.save(newUser);
        }
    }
}
