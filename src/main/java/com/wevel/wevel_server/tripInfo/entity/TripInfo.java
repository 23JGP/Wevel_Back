package com.wevel.wevel_server.tripInfo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class TripInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long tripId;

    @Column
    private Long userId;

    @Column
    private Long totalBudget;

    @Column
    private String tripName;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column(nullable = true)
    private Long spentAmount;

    @Column(nullable = true)
    private Long remainingAmount;

}