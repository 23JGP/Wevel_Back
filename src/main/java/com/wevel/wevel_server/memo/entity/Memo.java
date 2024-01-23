package com.wevel.wevel_server.memo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wevel.wevel_server.receipt.entity.Receipt;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id")
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

    // 생성자 수정
    public Memo(Long userId, String tripName, Receipt receipt, Date date, String receivedMemos, String givenMemos, Object o, Object o1) {
        this.userId = userId;
        this.tripName = tripName;
        this.receipt = receipt;
        this.date = date;
        this.amountReceived = receivedMemos;
        this.amountGiven = givenMemos;
        this.Rcompleted = false;
        this.Gcompleted = false;
    }
}
