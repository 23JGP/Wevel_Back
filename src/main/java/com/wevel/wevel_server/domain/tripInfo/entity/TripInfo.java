package com.wevel.wevel_server.domain.tripInfo.entity;

import com.wevel.wevel_server.domain.tripInfo.service.TripInfoService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@EntityListeners(TripInfoService.class)
public class TripInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long tripId; // 여행 pk 아이디

    @Column
    private Long userId; // 로그인한 유저 아이디

    @Column
    private double totalBudget; // 여행의 총 예산

    @Column
    private String tripName; // 여행 이름

    @Column
    private String country; // 여행 가는 나라 이름

    @Column
    private Date startDate; // 여행 시작 날짜

    @Column
    private Date endDate; // 여행 마지막 날짜

    @Column(nullable = true)
    private double spentAmount; // 여행 중 사용한 예산

    @Column(nullable = true)
    private double remainingAmount; // 여행 중 남은 예산

}