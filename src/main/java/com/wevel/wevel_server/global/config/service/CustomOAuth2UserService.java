package com.wevel.wevel_server.global.config.service;

import com.wevel.wevel_server.domain.user.entity.User;
import com.wevel.wevel_server.domain.user.service.UserFindService;
import com.wevel.wevel_server.domain.user.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRegistrationService userRegistrationService;
    private final UserFindService userFindService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User);

        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        String socialUserId = oAuth2UserInfo.getProviderId();

        userRegistrationService.registerUser(name, email, socialUserId, registrationId);
        User user = userFindService.findBySocialId(socialUserId);
        if (user != null) {
            log.info("User ID {} found for socialId {}", user.getId(), socialUserId);
        } else {
            log.error("User not found after registration: {}", socialUserId);
        }

        return oAuth2User; // OAuth2User 객체 반환
    }
}
