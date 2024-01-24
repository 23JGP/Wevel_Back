
package com.wevel.wevel_server.tripInfo.service;

import com.wevel.wevel_server.tripInfo.dto.TripInfoDTO;
import com.wevel.wevel_server.tripInfo.entity.TripInfo;
import com.wevel.wevel_server.tripInfo.repository.TripInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripInfoService {
    @Autowired
    private TripInfoRepository tripInfoRepository;

//    public List<TripInfo> getLatestTripByUserId(Long userId) {
//        try {
//            Date currentDate = new Date();
//            return tripInfoRepository.findByUserIdAndStartDateBeforeOrderByStartDateDesc(userId, currentDate);
//        } catch (Exception e) {
//            // 예외 로깅 또는 처리
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//    }

    public List<TripInfo> getRecentTripsByUserId(Long userId) {
        return tripInfoRepository.findByUserIdOrderByStartDateDesc(userId);
    }

    public List<TripInfoDTO> getTripInfoByUserId(Long userId, String orderBy) {
        List<TripInfoDTO> tripInfoDTOs = null;

        switch (orderBy) {
            case "recent":
                tripInfoDTOs = tripInfoRepository.findByUserIdOrderByStartDateDesc(userId)
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                break;
            case "oldest":
                tripInfoDTOs = tripInfoRepository.findByUserIdOrderByStartDateAsc(userId)
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                break;
            case "asc":
                tripInfoDTOs = tripInfoRepository.findByUserIdOrderByTripNameAsc(userId)
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                break;
            case "desc":
                tripInfoDTOs = tripInfoRepository.findByUserIdOrderByTripNameDesc(userId)
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 orderBy 매개변수입니다");
        }

        return tripInfoDTOs;
    }


    public TripInfoDTO updateTripInfo(Long userId, Long tripId, TripInfoDTO updatedTripInfoDTO) {
        TripInfo tripInfo = tripInfoRepository.findByUserIdAndTripId(userId, tripId)
                .orElseThrow(() -> new IllegalArgumentException("TripInfo not found for userId: " + userId + " and tripId: " + tripId));

        // Update the fields if provided in the request
        if (updatedTripInfoDTO.getTripName() != null) {
            tripInfo.setTripName(updatedTripInfoDTO.getTripName());
        }
        if (updatedTripInfoDTO.getStartDate() != null) {
            tripInfo.setStartDate(updatedTripInfoDTO.getStartDate());
        }
        if (updatedTripInfoDTO.getEndDate() != null) {
            tripInfo.setEndDate(updatedTripInfoDTO.getEndDate());
        }
        if (updatedTripInfoDTO.getTotalBudget() != null) {
            tripInfo.setTotalBudget(updatedTripInfoDTO.getTotalBudget());
        }

        tripInfo = tripInfoRepository.save(tripInfo);

        return convertToDTO(tripInfo);
    }

    private TripInfoDTO convertToDTO(TripInfo tripInfo) {
        return new TripInfoDTO(
                tripInfo.getTripName(),
                tripInfo.getTotalBudget(),
                tripInfo.getStartDate(),
                tripInfo.getEndDate()
        );
    }

}