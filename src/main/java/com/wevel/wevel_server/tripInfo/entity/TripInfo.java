package com.wevel.wevel_server.tripInfo.entity;

import com.wevel.wevel_server.receipt.entity.Receipt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
    private double totalBudget;

    @Column
    private String tripName;

    @Column
    private String country;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column(nullable = true)
    private double spentAmount;

    @Column(nullable = true)
    private double remainingAmount;

}