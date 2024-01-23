package com.wevel.wevel_server.memo.entity;

import com.wevel.wevel_server.receipt.entity.Receipt;
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

    @ManyToOne(targetEntity = Receipt.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", insertable = false, updatable = false)
    private Receipt receipt;  // Receipt 엔터티와의 조인을 위한 필드


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

    public Memo(Long userId, String tripName, Long receipt_id, Date date, String amountReceived, String amountGiven, Object o, Object o1) {
    }

}
