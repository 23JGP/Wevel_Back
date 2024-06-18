package com.wevel.wevel_server.domain.alarm.repository;

import com.wevel.wevel_server.domain.alarm.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    List<Notify> findByUserId(Long userId);
}
