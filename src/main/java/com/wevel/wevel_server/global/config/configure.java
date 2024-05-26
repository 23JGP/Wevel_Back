package com.wevel.wevel_server.global.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface configure {
    void configure(HttpSecurity http) throws Exception;
}
