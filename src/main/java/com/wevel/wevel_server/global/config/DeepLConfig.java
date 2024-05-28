package com.wevel.wevel_server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeepLConfig {

    @Value("${deepl.auth-key}")
    private String authKey;

    public String getAuthKey() {
        return authKey;
    }
}