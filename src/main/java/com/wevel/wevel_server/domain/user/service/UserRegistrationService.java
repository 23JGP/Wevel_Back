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
        if (existingUser == null) {
            // 사용자가 존재하지 않는 경우에만 새로운 사용자를 생성하고 저장
            User newUser = new User(name, email, socialId, provider);
            userRepository.save(newUser);
        }
    }
}
