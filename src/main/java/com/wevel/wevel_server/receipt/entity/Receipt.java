package com.wevel.wevel_server.receipt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long receiptId;

    @Column
    private Long userId;

    @Column
    private String tripName;

    @Column
    private String title;

    @Column
    private String productName;

    @Column
    private Long price;

    @Column
    private Integer quantity;

    @Column
    private Integer tax;

    @Column
    private Date date;

    public Receipt() {
        // 기본 생성자 추가
    }
}
