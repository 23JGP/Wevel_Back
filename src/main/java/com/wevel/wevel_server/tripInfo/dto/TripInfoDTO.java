package com.wevel.wevel_server.tripInfo.dto;

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
    private String tripName;
    private Long totalBudget;
    private Date startDate;
    private Date endDate;
}
