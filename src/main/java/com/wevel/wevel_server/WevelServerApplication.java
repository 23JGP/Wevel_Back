package com.wevel.wevel_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// spring security 로그인 화면 안뜨게
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WevelServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WevelServerApplication.class, args);
	}

}
