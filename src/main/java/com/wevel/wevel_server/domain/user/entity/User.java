package com.wevel.wevel_server.domain.user.entity;


import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "provider_id", nullable = false)
    private String socialId;

    @Column
    private String provider;

    public User(String name, String email, String socialId, String provider) {
        this.name = name;
        this.email = email;
        this.socialId = socialId;
        this.provider = provider;
    }
}
