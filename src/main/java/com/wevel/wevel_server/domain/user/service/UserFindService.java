package com.wevel.wevel_server.domain.user.service;

import com.wevel.wevel_server.domain.user.entity.User;
import com.wevel.wevel_server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFindService {
    private final UserRepository userRepository;

    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }
}
