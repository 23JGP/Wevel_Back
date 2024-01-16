package com.wevel.wevel_server.service;

import com.wevel.wevel_server.dto.OAuthAttributes;
import com.wevel.wevel_server.dto.SessionUser;
import com.wevel.wevel_server.entity.Users;
import com.wevel.wevel_server.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Lazy
@Service
@RequiredArgsConstructor
public class UsersService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final HttpSession httpSession;
    private final UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        // naver, kakao 로그인 구분
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Users user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private Users saveOrUpdate(OAuthAttributes attributes) {
        Users user = usersRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getEmail()))
                .orElse(attributes.toEntity());

        return usersRepository.save(user);
    }

    @Transactional
    public void createUser(SessionUser usersDTO) {
        try {
            Users user = new Users();
            BeanUtils.copyProperties(usersDTO, user);
            usersRepository.save(user);
        } catch (Exception e) {
            // 예외 처리 로직 추가 (필요에 따라 로깅 등)
            throw new RuntimeException("유저 추가에 실패했습니다.", e);
        }
    }
}
