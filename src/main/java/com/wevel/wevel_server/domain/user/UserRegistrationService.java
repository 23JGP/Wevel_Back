package com.wevel.wevel_server.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    public void registerUser(String name, String email) {
        User user = userFindService.findByEmail(email);

        if (user == null) {
            user = new User(name, email);
            userRepository.save(user);
        }
    }
}
