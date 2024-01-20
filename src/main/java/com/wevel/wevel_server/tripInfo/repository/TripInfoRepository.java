package com.wevel.wevel_server.tripInfo.repository;

import com.wevel.wevel_server.tripInfo.entity.TripInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TripInfoRepository extends JpaRepository<TripInfo, Long> {
    List<TripInfo> findByUserIdAndStartDateBeforeOrderByStartDateDesc(Long userId, Date startDate);
}