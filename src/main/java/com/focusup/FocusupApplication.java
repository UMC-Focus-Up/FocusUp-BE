package com.focusup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = {"com.focusup.global.security.feign"})
public class FocusupApplication {

	public static void main(String[] args) {
		SpringApplication.run(FocusupApplication.class, args);
	}

}
