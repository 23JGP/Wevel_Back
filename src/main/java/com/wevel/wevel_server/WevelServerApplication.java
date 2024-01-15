package com.wevel.wevel_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// spring security 로그인 화면 안뜨게
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WevelServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WevelServerApplication.class, args);
	}

}
