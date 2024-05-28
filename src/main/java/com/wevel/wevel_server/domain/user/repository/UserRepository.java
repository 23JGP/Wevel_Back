package com.wevel.wevel_server.domain.user.repository;

import com.wevel.wevel_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findById(Long userId);
    User findByEmail(String userEmail);
}