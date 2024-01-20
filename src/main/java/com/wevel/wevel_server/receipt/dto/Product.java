package com.wevel.wevel_server.receipt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String productName;
    private int quantity;
    private double price;

    // 생성자, 게터 및 세터 생략 가능
}