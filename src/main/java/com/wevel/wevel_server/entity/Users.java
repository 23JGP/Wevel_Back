package com.wevel.wevel_server.entity;

import com.wevel.wevel_server.role.Role;
import io.micrometer.common.util.internal.logging.InternalLogger;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.User;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    public Users update(String name, String email) {
        this.name = name;
        this.email = email;

        return this;
    }
    public String getRoleKey() {
        return this.role.getKey();
    }
}
