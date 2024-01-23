
package com.wevel.wevel_server.tripInfo.service;

import com.wevel.wevel_server.tripInfo.entity.TripInfo;
import com.wevel.wevel_server.tripInfo.repository.TripInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TripInfoService {
    @Autowired
    private TripInfoRepository tripInfoRepository;

    public List<TripInfo> getLatestTripByUserId(Long userId) {
        Date currentDate = new Date();
        return tripInfoRepository.findByUserIdAndStartDateBeforeOrderByStartDateDesc(userId, currentDate);
    }

    public List<TripInfo> getTripInfoByUserId(Long userId, String orderBy) {
        switch (orderBy) {
            case "recent":
                return tripInfoRepository.findByUserIdOrderByStartDateDesc(userId);
            case "oldest":
                return tripInfoRepository.findByUserIdOrderByStartDateAsc(userId);
            case "asc":
                return tripInfoRepository.findByUserIdOrderByTripNameAsc(userId);
            case "desc":
                return tripInfoRepository.findByUserIdOrderByTripNameDesc(userId);
            default:
                throw new IllegalArgumentException("유효하지 않은 orderBy 매개변수입니다");
        }
    }

}