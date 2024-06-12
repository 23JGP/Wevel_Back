package com.wevel.wevel_server.global.config.service;

public interface OAuth2UserInfo {
    String getProvider();
    String getEmail();
    String getName();
}