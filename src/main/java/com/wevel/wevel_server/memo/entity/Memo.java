package com.wevel.wevel_server.memo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long memoId;

    @Column
    private Long userId;

    @Column
    private String tripName;

    @Column
    private Long receiptId;

    @Column
    private Date date;

    @Column
    private String amountReceived;

    @Column
    private String amountGiven;

    @Column(nullable = true)
    private Boolean Rcompleted;

    @Column(nullable = true)
    private Boolean Gcompleted;
}
