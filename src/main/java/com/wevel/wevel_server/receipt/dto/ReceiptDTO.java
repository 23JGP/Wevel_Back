package com.wevel.wevel_server.receipt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReceiptDTO {

    private Long receiptId;
    private Long userId;
    private Long tripId;
    private String title;
    private String productName;
    private Long price;
    private Integer quantity;
    private Integer tax;
    private Date date;

}