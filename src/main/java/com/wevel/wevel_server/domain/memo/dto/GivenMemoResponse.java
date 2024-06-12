package com.wevel.wevel_server.domain.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GivenMemoResponse {
    private Long memoId;
    private String amountGiven;
    private Boolean Gcompleted;
    private Date date;
}
