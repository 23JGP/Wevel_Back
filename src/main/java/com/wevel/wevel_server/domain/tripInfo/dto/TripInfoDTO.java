package com.wevel.wevel_server.domain.tripInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripInfoDTO {
    private Long tripId;
    private String tripName;
    private String country;
    private double totalBudget;
    private Date startDate;
    private Date endDate;

}
