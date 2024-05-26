package com.wevel.wevel_server.domain.memo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wevel.wevel_server.domain.receipt.entity.Receipt;
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


    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiptId")
    private Receipt receipt;

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

    public Memo(Long userId, String tripName, Receipt receipt, Date date, String receivedMemos, String givenMemos, Object o, Object o1) {
        this.userId = userId;
        this.tripName = tripName;
        this.receipt = receipt;
        this.date = date;
        this.amountReceived = receivedMemos;
        this.amountGiven = givenMemos;
        this.Rcompleted = Boolean.FALSE;  // Boolean 타입의 경우 TRUE 또는 FALSE로 설정
        this.Gcompleted = Boolean.FALSE;  // Boolean 타입의 경우 TRUE 또는 FALSE로 설정
    }

}
