package com.wevel.wevel_server.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GivenMemoResponse {
    private String amountGiven;
    private Boolean Gcompleted;
    private Date date;
}
