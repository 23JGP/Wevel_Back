package com.wevel.wevel_server.domain.deepapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DeepLConfig {

    @Value("${deepl.auth-key}")
    private String authKey;

    public String getAuthKey() {
        return authKey;
    }
}