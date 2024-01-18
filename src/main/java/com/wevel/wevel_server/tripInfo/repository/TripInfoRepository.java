package com.wevel.wevel_server.tripInfo.repository;

import com.wevel.wevel_server.tripInfo.entity.TripInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripInfoRepository extends JpaRepository<TripInfo, Long> {

}
