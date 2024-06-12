package com.wevel.wevel_server.global.config;

import com.wevel.wevel_server.domain.alarm.service.AlarmService;
import com.wevel.wevel_server.domain.user.entity.User;
import com.wevel.wevel_server.domain.user.service.UserFindService;
import com.wevel.wevel_server.domain.user.service.UserRegistrationService;
import com.wevel.wevel_server.global.config.service.CustomOAuth2UserService;
import com.wevel.wevel_server.global.config.service.OAuth2UserInfo;
import com.wevel.wevel_server.global.config.service.OAuth2UserInfoFactory;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> implements config.SecurityConfig {

    private final Environment environment;
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final AlarmService alarmService;
    private final String registration = "spring.security.oauth2.client.registration.";
    private final HttpSession session;
    private final UserFindService userFindService;
    private final UserRegistrationService userRegistrationService;

    private static final String[] AUTH_WHITELIST = {
            "/api/**", "/graphiql", "/graphql",
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html"
    };


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers("/resources/**")
                .requestMatchers("/h2-console/**")
                .requestMatchers("/api/**")
                .requestMatchers("/swagger-ui/**")
                .requestMatchers("/swagger-ui.html") // 추가
                .requestMatchers("/v3/api-docs/**"); // 추가
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/api/user/me").authenticated()
                        .requestMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(PathRequest.toH2Console())
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(clientRegistrationRepository())
                        .authorizedClientService(auth2AuthorizedClientService())
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
                        )
                        .successHandler((request, response, authentication) -> {
                            if (authentication == null) {
                                log.error("Authentication is null");
                                throw new NullPointerException("Authentication is null");
                            }

                            HttpSession session = request.getSession();
                            log.info("Session ID: {}", session.getId());

                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

                            OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User);

                            String providerUserId = oAuth2UserInfo.getProviderId(); // 소셜 미디어 플랫폼에서 제공하는 고유한 사용자 ID
                            if (providerUserId == null) {
                                log.error("Email not found for registrationId: {}", registrationId);
                                response.sendRedirect("http://localhost:3000/html/login.html");
                                return;
                            }

                            User user = userFindService.findBySocialId(providerUserId);
                            if (user != null) {
                                Long userId = user.getId();
                                session.setAttribute("userId", userId);
                                session.setAttribute("social", registrationId);
                                log.info("User ID {} and social {} set in session.", userId, registrationId);

                                alarmService.createAlarmForUser(userId);
                                response.sendRedirect("http://localhost:3000/session.html?userId=" + userId);
                            } else {
                                log.error("User not found for providerUserId : {}", providerUserId);
                                response.sendRedirect("http://localhost:3000/html/login.html");
                            }
                        })
                )
                .sessionManagement(session -> session
                        .sessionFixation().newSession()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                );
        return http.build();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRegistrationService, userFindService);
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        System.out.println("success");
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

    @Bean
    public WebSecurityCustomizer configureApiReceipts() {
        return web -> web.ignoring()
                .requestMatchers("/api/**");
    }

    @Bean
    public OAuth2AuthorizedClientService auth2AuthorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        final List<ClientRegistration> clientRegistrations = Arrays.asList(
                googleClientRegistration(),
                naverClientRegistration(),
                kakaoClientRegistration()
        );
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    // Google
    private ClientRegistration googleClientRegistration() {
        // yml에 있는 id, secret 가져오기
        final String clientId = environment.getProperty(registration + "google.client-id");
        final String clientSecret = environment.getProperty(registration + "google.client-secret");

        return CommonOAuth2Provider
                .GOOGLE
                .getBuilder("google")
                .clientId(clientId)
                .scope("email", "profile")
                .clientSecret(clientSecret)
                .build();
    }

    // Naver
    private ClientRegistration naverClientRegistration() {
        String clientId = environment.getProperty(registration + "naver.client-id");
        String clientSecret = environment.getProperty(registration + "naver.client-secret");
        String redirectUri = environment.getProperty(registration + "naver.redirect-uri");

        return ClientRegistration.withRegistrationId("naver")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(redirectUri)
                .scope("email", "name")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response")
                .clientName("Naver")
                .build();
    }

    // Kakao
    private ClientRegistration kakaoClientRegistration() {
        String clientId = environment.getProperty(registration + "kakao.client-id");
        String clientSecret = environment.getProperty(registration + "kakao.client-secret");
        String redirectUri = environment.getProperty(registration + "kakao.redirect-uri");

        return ClientRegistration.withRegistrationId("kakao")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(redirectUri)
                .scope("profile_nickname", "account_email") // 동의 항목 설정
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id") // 사용자 식별자 속성 설정
                .clientName("Kakao")
                .build();
    }
}