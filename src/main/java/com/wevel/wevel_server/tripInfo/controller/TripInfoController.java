package com.wevel.wevel_server.tripInfo.controller;

import com.wevel.wevel_server.tripInfo.dto.SpentPercentageResponse;
import com.wevel.wevel_server.tripInfo.dto.TripInfoDTO;
import com.wevel.wevel_server.tripInfo.entity.TripInfo;
import com.wevel.wevel_server.tripInfo.repository.TripInfoRepository;
import com.wevel.wevel_server.tripInfo.service.TripInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/trips")
public class TripInfoController {

    @Autowired
    private TripInfoRepository tripInfoRepository;
    @Autowired
    private TripInfoService tripService;


    // 세션에서 사용자의 userId 가져오는 메서드
    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // 여행 탭 -> 폴더추가 -> 여행 정보 추가하기 : post = /api/trips/create
    @PostMapping("/create")
    public ResponseEntity<TripInfo> createTripInfo(@RequestBody TripInfo tripInfo) {
        // 세션에서 사용자의 userId 가져오기
//        Long userId = getCurrentUserId();
        Long userId = 1L;
        tripInfo.setUserId(userId);

        // 기타 초기화 로직 추가
        TripInfo createdTripInfo = tripInfoRepository.save(tripInfo);
        return ResponseEntity.ok(createdTripInfo);
    }

    // 홈페이지 -> 유저아이디와 현재 날짜를 입력하면 로그인한 사용자의 최근 여행정보를 가져오기
    // get = /api/trips/latest:userId
    @GetMapping("/latest/{userId}")
    public TripInfo getLatestTripByUserId(@PathVariable Long userId) {
        return tripService.getLatestTripByUserId(userId).get(0);
    }

    // ____ -> 사용한 금액 퍼센트화 get = /api/trips/latest/:id/spentPercentage
    @GetMapping("/latest/{userId}/spentPercentage")
    public ResponseEntity<SpentPercentageResponse> getSpentPercentageByUserId(@PathVariable Long userId) {
        List<TripInfo> recentTrips = tripService.getLatestTripByUserId(userId);

        if (!recentTrips.isEmpty()) {
            TripInfo mostRecentTrip = recentTrips.get(0);

            if (mostRecentTrip.getTotalBudget() != null && mostRecentTrip.getSpentAmount() != null) {
                double totalBudget = mostRecentTrip.getTotalBudget();
                double spentAmount = mostRecentTrip.getSpentAmount();

                double spentPercentage = (spentAmount / totalBudget) * 100;

                // SpentPercentageResponse 객체를 생성하여 JSON으로 반환
                SpentPercentageResponse response = new SpentPercentageResponse(spentPercentage);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 여행페이지 -> 유저아이디를 입력받으면 최근순, 오래된 순, 오름 차순 , 내림차순으로 정렬
    // 최근 순 : get = /api/trips/trip-info/:userId?orderBy=recent
    // 오래된 순 : get = /api/trips/trip-info/:userId?orderBy=oldest
    // 오름차순 : get = /api/trips/trip-info/:userId?orderBy=asc
    // 내림차순 : get = /api/trips/trip-info/:userId?orderBy=desc
    @GetMapping("/trip-info/{userId}")
    public List<TripInfoDTO> getTripInfoByUserId(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "recent") String orderBy) {
        return tripService.getTripInfoByUserId(userId, orderBy);
    }

    @PutMapping("/trip-info/{userId}/{tripId}")
    public TripInfoDTO updateTripInfo(@PathVariable Long userId,
                                      @PathVariable Long tripId,
                                      @RequestBody TripInfoDTO updatedTripInfoDTO) {
        return tripService.updateTripInfo(userId, tripId, updatedTripInfoDTO);
    }

    // TODO : 수정 예정 -> 유저 아이디 추가
    // 모든 여행 목록 조회
    @GetMapping
    public List<TripInfo> getAllTrips(@PathVariable("userId") Long userId) {
        return tripInfoRepository.findAll();
    }

    // 특정 여행 조회
    @GetMapping("/{tripId}")
    public ResponseEntity<TripInfo> getTripById(@PathVariable Long tripId) {
        return tripInfoRepository.findById(tripId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 여행 생성
    @PostMapping
    public ResponseEntity<TripInfo> createTrip(@RequestBody TripInfo tripInfo) {
        TripInfo createdTrip = tripInfoRepository.save(tripInfo);
        return ResponseEntity.ok(createdTrip);
    }

    // 여행 수정
    @PutMapping("/{tripId}")
    public ResponseEntity<TripInfo> updateTrip(@PathVariable Long tripId, @RequestBody TripInfo updatedTripinfo) {
        return tripInfoRepository.findById(tripId)
                .map(existingTrip -> {
                    existingTrip.setTotalBudget(updatedTripinfo.getTotalBudget());
                    existingTrip.setTripName(updatedTripinfo.getTripName());
                    existingTrip.setStartDate(updatedTripinfo.getStartDate());
                    existingTrip.setEndDate(updatedTripinfo.getEndDate());
                    existingTrip.setSpentAmount(updatedTripinfo.getSpentAmount());
                    existingTrip.setRemainingAmount(updatedTripinfo.getRemainingAmount());
                    TripInfo savedTrip = tripInfoRepository.save(existingTrip);
                    return ResponseEntity.ok(savedTrip);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 여행 삭제
    @DeleteMapping("/{tripId}")
    public ResponseEntity<Object> deleteTrip(@PathVariable Long tripId) {
        return tripInfoRepository.findById(tripId)
                .map(existingTrip -> {
                    tripInfoRepository.delete(existingTrip);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}