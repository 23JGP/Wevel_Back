package com.wevel.wevel_server.domain.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyDto {
    private Long id;
    private Long userId;
    private Long tripId;
    private String message;
    private Boolean isLead;
    private String dateFormatted;
}