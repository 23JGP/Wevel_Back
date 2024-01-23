package com.wevel.wevel_server.receipt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long productId;

    @Column
    private String productName;

    @Column
    private double price;

    @Column
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;
}