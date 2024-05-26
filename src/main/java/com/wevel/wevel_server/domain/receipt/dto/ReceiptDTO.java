package com.wevel.wevel_server.domain.receipt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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