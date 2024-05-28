package com.wevel.wevel_server.domain.tripInfo;

import com.wevel.wevel_server.domain.tripInfo.dto.TripInfoDTO;
import com.wevel.wevel_server.domain.tripInfo.entity.TripInfo;
import com.wevel.wevel_server.domain.tripInfo.repository.TripInfoRepository;
import com.wevel.wevel_server.domain.tripInfo.service.TripInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/trips")
@Tag(name = "Trip API", description = "여행일지(폴더) 관련 API")
public class TripInfoController {

    @Autowired
    private TripInfoRepository tripInfoRepository;

    @Autowired
    private TripInfoService tripService;

    // 여행 탭 -> 폴더추가 -> 여행 정보 추가하기 : post = /api/trips
    @PostMapping
    public ResponseEntity<TripInfo> createTripInfo(@RequestBody TripInfo tripInfo) {
        TripInfo createdTripInfo = tripInfoRepository.save(tripInfo);
        return ResponseEntity.ok(createdTripInfo);
    }

    // 홈페이지 -> 유저아이디와 현재 날짜를 입력하면 로그인한 사용자의 최근 여행정보를 가져오기
    @GetMapping("/latest/{userId}")
    public TripInfo getRecentTripByUserId(@PathVariable Long userId) {
        List<TripInfo> recentTrips = tripService.getRecentTripsByUserId(userId);
        if (!recentTrips.isEmpty()) {
            // 여러 개의 여행 정보가 있다면 가장 최근 것 하나만 반환
            return recentTrips.get(0);
        } else {
            // 여행 정보가 없을 경우 null
            return null;
        }
    }

    // 여행페이지 -> 유저아이디를 입력받으면 최근순, 오래된 순, 오름 차순 , 내림차순으로 정렬
    // 최근 순 : get = /api/trips/info/:userId?orderBy=recent
    // 오래된 순 : get = /api/trips/info/:userId?orderBy=oldest
    // 오름차순 : get = /api/trips/info/:userId?orderBy=asc
    // 내림차순 : get = /api/trips/info/:userId?orderBy=desc
    @GetMapping("/info/{userId}")
    public List<TripInfoDTO> getTripInfoByUserId(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "recent") String orderBy) {
        return tripService.getTripInfoByUserId(userId, orderBy);
    }

    // 마이페이지 -> 전체 여행 정보의 개수를 알려주는 API 엔드포인트
    @GetMapping("/count/{userId}")
    public Long getCountOfTrips(@PathVariable("userId") Long userId) {
        // 여행 정보의 개수 반환
        return tripInfoRepository.countByUserId(userId);
    }

    // 모든 여행 목록 조회
    @GetMapping
    public List<TripInfo> getAllTrips(@PathVariable("userId") Long userId) {
        return tripInfoRepository.findAll();
    }

    // 여행 단일 조회
    @GetMapping("/{tripId}")
    public ResponseEntity<TripInfo> getTripById(@PathVariable Long tripId) {
        return tripInfoRepository.findById(tripId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 특정 여행 수정
    @PatchMapping("/{tripId}")
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

    // 특정 여행 삭제
    @DeleteMapping("/{tripId}")
    public ResponseEntity<Object> deleteTrip(@PathVariable Long tripId) {
        return tripInfoRepository.findById(tripId)
                .map(existingTrip -> {
                    tripInfoRepository.delete(existingTrip);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


//    // 특정 유저의 여행기간이 최근순인 tripId를 반환하는 API 엔드포인트
//    @GetMapping("/latest/{userId}")
//    public ResponseEntity<Long> getLatestTripIdByUserId(@PathVariable Long userId) {
//        TripInfo latestTrip = tripInfoRepository.findFirstByUserIdOrderByStartDateDesc(userId);
//        if (latestTrip != null) {
//            return ResponseEntity.ok(latestTrip.getTripId());
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

}