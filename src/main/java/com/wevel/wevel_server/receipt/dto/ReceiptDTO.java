package com.wevel.wevel_server.receipt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ReceiptDTO {

    private Long receiptId;
    private Long userId;
    private Date date;
    private String tripName;
    private String title;
    private List<ProductDTO> productDTOList;
    private int tax;
    private int sum;
    private String receivedMemos;
    private String givenMemos;

}