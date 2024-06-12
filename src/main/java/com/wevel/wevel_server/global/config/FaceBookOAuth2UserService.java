package com.wevel.wevel_server.global.config;

import com.wevel.wevel_server.domain.user.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaceBookOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRegistrationService userRegistrationService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = userService.loadUser(userRequest);

        final String name = oAuth2User.getAttributes().get("name").toString();
        final String email = oAuth2User.getAttributes().get("email").toString();

        userRegistrationService.registerUser(name, email, "FACEBOOK");

        return new DefaultOAuth2User(
              oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "id"
        );
    }
}
