package com.wevel.wevel_server.domain.memo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wevel.wevel_server.domain.receipt.entity.Receipt;
import jakarta.persistence.*;
import lombok.*;

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
    private Long tripId;


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

}
