package com.wevel.wevel_server.memo.repository;

import com.wevel.wevel_server.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByUserIdAndTripName(Long userId, String tripName);
}
