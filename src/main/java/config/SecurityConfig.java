package config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface SecurityConfig {
    void configure(HttpSecurity http) throws Exception;

    void configure(WebSecurity web) throws Exception;
}
