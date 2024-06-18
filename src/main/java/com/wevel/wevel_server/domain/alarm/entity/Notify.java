package com.wevel.wevel_server.domain.alarm.entity;

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
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long tripId;

    @Column
    private Date date;

    @Column
    private String message;

    @Column
    private String type;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isLead;
}
