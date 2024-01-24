package com.wevel.wevel_server.tripInfo.repository;

import com.wevel.wevel_server.tripInfo.entity.TripInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface TripInfoRepository extends JpaRepository<TripInfo, Long> {
    List<TripInfo> findByUserIdOrderByStartDateDesc(Long userId);
    List<TripInfo> findByUserIdOrderByStartDateAsc(Long userId);
    List<TripInfo> findByUserIdOrderByTripNameAsc(Long userId);
    List<TripInfo> findByUserIdOrderByTripNameDesc(Long userId);
    Optional<TripInfo> findByUserIdAndTripId(Long userId, Long tripId);
    List<TripInfo> findByUserIdAndStartDateBeforeOrderByStartDateDesc(Long userId, Date currentDate);
}