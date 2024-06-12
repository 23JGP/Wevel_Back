package com.wevel.wevel_server.domain.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReceivedMemoResponse {
    private Long memoId;
    private String amountReceived;
    private Boolean Rcompleted;
    private Date date;
}
