package com.wevel.wevel_server.service;

import com.wevel.wevel_server.dto.UsersDTO;
import com.wevel.wevel_server.dto.UsersDTO;
import com.wevel.wevel_server.entity.Users;
import com.wevel.wevel_server.repository.UsersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void createUser(UsersDTO usersDTO) {
        try {
            Users user = new Users();
            BeanUtils.copyProperties(usersDTO, user);
            usersRepository.save(user);
        } catch (Exception e) {
            // 예외 처리 로직 추가 (필요에 따라 로깅 등)
            throw new RuntimeException("유저 추가에 실패했습니다.", e);
        }
    }

    // 다른 메소드들을 추가할 수 있습니다.
}
