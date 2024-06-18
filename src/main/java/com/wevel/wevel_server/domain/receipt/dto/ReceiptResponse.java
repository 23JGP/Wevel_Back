package com.wevel.wevel_server.domain.receipt.dto;

import lombok.Data;

@Data
public class ReceiptResponse {
    private long tripId;
    private long receiptId;

    private String title;
    private String date;
    private Double total;
}
