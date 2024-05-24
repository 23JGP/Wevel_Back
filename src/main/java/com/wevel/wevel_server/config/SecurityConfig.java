package com.wevel.wevel_server.config;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
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
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.util.WebUtils.getSessionId;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> implements config.SecurityConfig {

    private final Environment environment;
    private final String registration = "spring.security.oauth2.client.registration.";

    private final FaceBookOAuth2UserService faceBookOAuth2UserService;
    private final GoogleOAuth2UserService googleOAuth2UserService;

    private static final String[] AUTH_WHITELIST = {
            "/api/**", "/graphiql", "/graphql",
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(
                        authorize -> authorize
                                .shouldFilterAllDispatcherTypes(false)
                                .requestMatchers(AUTH_WHITELIST)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic().disable()
                .formLogin().disable()
                .cors().disable()
                .csrf().disable()
                .build();
    }

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

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(PathRequest.toH2Console()))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer
                                .FrameOptionsConfig::sameOrigin))
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(clientRegistrationRepository())
                        .authorizedClientService(auth2AuthorizedClientService())
                        .userInfoEndpoint( user -> user
                                .oidcUserService(googleOAuth2UserService) // google 인증 , OpenId Connect 1.0
                                .userService(faceBookOAuth2UserService) // facebook 인증, OAuth2 통신
                        )
                        .successHandler((request, response, authentication) -> {
                            // 쿠키를 생성하여 세션 ID를 설정
                            Cookie cookie = new Cookie("sessionId", getSessionId(request));
                            cookie.setPath("/"); // 쿠키의 유효 경로를 설정
                            response.addCookie(cookie);

                            // 리다이렉션
                            response.sendRedirect("http://localhost:3000/index.html");
                        })

                        .defaultSuccessUrl("http://localhost:3000/index.html", true) // defaultSuccessURL 설정
                );
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
                facebookClientRegistration()
        );

        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    private ClientRegistration googleClientRegistration() {
        // yml에 있는 id, secret 가져오기
        final String clientId = environment.getProperty(registration + "google.client-id");
        final String clientSecret = environment.getProperty(registration + "google.client-secret");

        return CommonOAuth2Provider
                .GOOGLE
                .getBuilder("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }


    private ClientRegistration facebookClientRegistration() {
        // yml에 있는 id, secret 가져오기
        final String clientId = environment.getProperty(registration + "facebook.client-id");
        final String clientSecret = environment.getProperty(registration + "facebook.client-secret");

        return CommonOAuth2Provider
                .FACEBOOK
                .getBuilder("facebook")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scope(
                        "public_profile",
                        "email",
                        "user_birthday",
                        "user_gender"
                )
                .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,user_birthday,user_gender")
                .build();
    }
}