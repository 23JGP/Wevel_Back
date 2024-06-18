package com.wevel.wevel_server.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long userId;

    @Column(columnDefinition = "boolean default false")
    private boolean overBudgetAlarm = false;

    @Getter
    @Column(columnDefinition = "boolean default false")
    private boolean spendAlarm = false;

    public Alarm(Long userId) {
        this.userId = userId;
        this.overBudgetAlarm = false;
        this.spendAlarm = false;
    }
}