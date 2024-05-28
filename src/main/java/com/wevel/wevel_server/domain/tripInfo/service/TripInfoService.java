
package com.wevel.wevel_server.domain.tripInfo.service;

import com.wevel.wevel_server.domain.tripInfo.dto.TripInfoDTO;
import com.wevel.wevel_server.domain.tripInfo.entity.TripInfo;
import com.wevel.wevel_server.domain.tripInfo.repository.TripInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripInfoService {

    @Autowired
    private TripInfoRepository tripInfoRepository;


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
        TripInfo tripInfo = (TripInfo) tripInfoRepository.findByUserIdAndTripId(userId, tripId);

        if (updatedTripInfoDTO.getTripName() != null) {
            tripInfo.setTripName(updatedTripInfoDTO.getTripName());
        }
        if (updatedTripInfoDTO.getStartDate() != null) {
            tripInfo.setStartDate(updatedTripInfoDTO.getStartDate());
        }
        if (updatedTripInfoDTO.getEndDate() != null) {
            tripInfo.setEndDate(updatedTripInfoDTO.getEndDate());
        }
        if (updatedTripInfoDTO.getTotalBudget() != 0) {
            tripInfo.setTotalBudget(updatedTripInfoDTO.getTotalBudget());
        }

        tripInfo = tripInfoRepository.save(tripInfo);

        return convertToDTO(tripInfo);
    }

    private TripInfoDTO convertToDTO(TripInfo tripInfo) {
        return new TripInfoDTO(
                tripInfo.getTripId(),
                tripInfo.getTripName(),
                tripInfo.getCountry(),
                tripInfo.getTotalBudget(),
                tripInfo.getStartDate(),
                tripInfo.getEndDate()
        );
    }

    public List<TripInfo> findTripInfoByUserIdAndTripId(Long userId, Long tripId) {
        return tripInfoRepository.findByUserIdAndTripId(userId, tripId);
    }

    public void saveTripInfo(TripInfo newTripInfo) {
        tripInfoRepository.save(newTripInfo);
    }
}