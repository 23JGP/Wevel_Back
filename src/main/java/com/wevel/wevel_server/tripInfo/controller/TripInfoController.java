package com.wevel.wevel_server.tripInfo.controller;

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