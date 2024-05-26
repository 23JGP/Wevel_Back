package com.wevel.wevel_server.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    public void requestRegistration(
            final String name,
            final String email
    ){
      final boolean exists = userFindService.existsByEmail(email);

      if(exists == false) {
          final User user = new User(name, email);
          userRepository.save(user);
      }

    }
}
