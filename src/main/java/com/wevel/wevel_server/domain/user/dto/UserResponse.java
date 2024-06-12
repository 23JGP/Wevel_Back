package com.wevel.wevel_server.domain.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String name;
    private String social;
}
