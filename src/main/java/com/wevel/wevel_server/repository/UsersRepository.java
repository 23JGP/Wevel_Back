package com.wevel.wevel_server.repository;

import com.wevel.wevel_server.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    // 추가적인 사용자 관련 메소드 정의 가능
    Optional<Users> findByEmail(String email);
}
