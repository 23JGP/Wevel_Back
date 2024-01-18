package com.wevel.wevel_server.config;

import com.wevel.wevel_server.user.User;
import com.wevel.wevel_server.user.UserFindService;
import com.wevel.wevel_server.user.UserRegistrationService;
import com.wevel.wevel_server.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@AllArgsConstructor
public class GoogleOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRegistrationService userRegistrationService;
    private final UserRepository userRepository;
    private final UserFindService userFindService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final OidcUserService oidcUserService = new OidcUserService();
        final OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        final OAuth2AccessToken accessToken = userRequest.getAccessToken();

        final String name = oidcUser.getAttributes().get("name").toString();
        final String email = oidcUser.getAttributes().get("email").toString();


        userRegistrationService.requestRegistration(name, email);

        // 사용자 ID를 UserRepository를 사용하여 찾기
        User user = userFindService.findByEmail(email);

        System.out.println(user);

        if (user != null) {
            Long userId = user.getId();
            storeUserIdInSession(userId);
            System.out.println("session save ID: " + userId);
        }

        return new DefaultOidcUser(
                oidcUser.getAuthorities(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo()
        );
    }


    private void storeUserIdInSession(Long userId) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        session.setAttribute("userId", userId);
    }

}
