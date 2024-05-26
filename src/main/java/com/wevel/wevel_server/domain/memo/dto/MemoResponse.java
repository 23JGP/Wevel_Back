package com.wevel.wevel_server.domain.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MemoResponse {
    private String amountGiven;
    private String amountReceived;
    private Boolean Rcompleted;
    private Boolean Gcompleted;
    private Date date;
}