package com.wevel.wevel_server.global.config;

import com.wevel.wevel_server.global.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> implements config.SecurityConfig {

    private final Environment environment;
    private final String registration = "spring.security.oauth2.client.registration.";

    private final FaceBookOAuth2UserService faceBookOAuth2UserService;
    private final GoogleOAuth2UserService googleOAuth2UserService;

    @Autowired
    private TokenProvider tokenProvider;

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
                                .oidcUserService(googleOAuth2UserService)
                                .userService(faceBookOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            String token = tokenProvider.createToken(authentication);
                            response.setHeader("Authorization", "Bearer " + token);
                            log.info(token);
                            System.out.println("token : " + token);
                            response.sendRedirect("http://localhost:3000/html/travel.html");
                        })
//                        .defaultSuccessUrl("http://localhost:3000/html/travel.html", true)
                )
                .sessionManagement(session -> session
                        .sessionFixation(sessionFixation -> sessionFixation.newSession())
                        .maximumSessions(1)
                )
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public static class JwtFilter extends OncePerRequestFilter {

        private final TokenProvider tokenProvider;

        public JwtFilter(TokenProvider tokenProvider) {
            this.tokenProvider = tokenProvider;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String jwt = resolveToken(request);
            if (jwt != null && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }

        private String resolveToken(HttpServletRequest request) {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return null;
        }
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