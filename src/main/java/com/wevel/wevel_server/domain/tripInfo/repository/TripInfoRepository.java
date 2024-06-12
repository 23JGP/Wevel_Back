package com.wevel.wevel_server.domain.tripInfo.repository;

import com.wevel.wevel_server.domain.tripInfo.entity.TripInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TripInfoRepository extends JpaRepository<TripInfo, Long> {
    List<TripInfo> findByUserIdOrderByStartDateDesc(Long userId);
    List<TripInfo> findByUserIdOrderByStartDateAsc(Long userId);
    List<TripInfo> findByUserIdOrderByTripNameAsc(Long userId);
    List<TripInfo> findByUserIdOrderByTripNameDesc(Long userId);
    List<TripInfo> findByUserIdAndTripId(Long userId, Long tripId);
    List<TripInfo> findByUserIdAndStartDateBeforeOrderByStartDateDesc(Long userId, Date currentDate);
    Long countByUserId(Long userId);

    TripInfo findFirstByOrderByStartDateDesc();
    TripInfo findFirstByUserIdOrderByStartDateDesc(Long userId);
//    TripInfo findByUserIdAndTripId(Long userId, Long tripId);
}