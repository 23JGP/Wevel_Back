package com.wevel.wevel_server.global.config;

import com.wevel.wevel_server.domain.user.entity.User;
import com.wevel.wevel_server.domain.user.service.UserFindService;
import com.wevel.wevel_server.domain.user.service.UserRegistrationService;
import com.wevel.wevel_server.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@AllArgsConstructor
public class GoogleOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRegistrationService userRegistrationService;
    private final UserRepository userRepository;
    private final UserFindService userFindService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUserService oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        String name = oidcUser.getAttributes().get("name").toString();
        String email = oidcUser.getAttributes().get("email").toString();

        userRegistrationService.registerUser(name, email, "GOOGLE");
        User user = userFindService.findByEmail(email);

        if (user != null) {
            log.info("User ID {} found for email {}", user.getId(), email);
        } else {
            log.error("User not found after registration: {}", email);
        }
        log.info(accessToken.toString());
        return new DefaultOidcUser(
                oidcUser.getAuthorities(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo()
        );
    }

}