package com.wevel.wevel_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "kakaoId")
    private String kakaoId;

    @Column(name = "naverToken")
    private String naverToken;

    @Column(name = "googleToken")
    private String googleToken;

    // getters and setters
}
