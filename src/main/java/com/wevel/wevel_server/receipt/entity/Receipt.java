package com.wevel.wevel_server.receipt.entity;

import com.wevel.wevel_server.memo.entity.Memo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
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

        @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @OneToOne(mappedBy = "receipt", cascade = CascadeType.ALL)
    private Memo memo;

    @Column
    private int tax;

    @Column
    private Date date;

}
