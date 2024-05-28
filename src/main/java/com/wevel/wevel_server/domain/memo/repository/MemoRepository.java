package com.wevel.wevel_server.domain.memo.repository;

import com.wevel.wevel_server.domain.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  MemoRepository extends JpaRepository<Memo, Long> {
//    List<Memo> findByUserIdAndTripName(Long userId, String tripName);
    List<Memo> findByUserIdAndTripId(Long userId, Long tripId);
}
