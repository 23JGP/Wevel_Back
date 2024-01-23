package com.wevel.wevel_server.receipt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String productName;
    private int quantity;
    private double price;

    // 생성자를 수정하여 값을 초기화하도록 함
    public ProductDTO(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // 기본 생성자 추가
    public ProductDTO() {
    }
}
