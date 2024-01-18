package com.wevel.wevel_server.dto;

import lombok.Getter;
import lombok.Setter;

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

}