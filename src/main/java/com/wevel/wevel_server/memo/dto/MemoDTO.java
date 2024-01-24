package com.wevel.wevel_server.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemoDTO {

    private Long memoId;
    private Long userId;
    private String tripName;
    private Date date;
    private String amountReceived;
    private String amountGiven;
}
