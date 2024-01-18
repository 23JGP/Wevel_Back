package config;

import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface SecurityConfig {
    void configure(HttpSecurity http) throws Exception;

    void onApplicationEvent(InteractiveAuthenticationSuccessEvent event);

    void configure(WebSecurity web) throws Exception;
}
