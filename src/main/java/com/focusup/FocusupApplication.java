package com.focusup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FocusupApplication {

	public static void main(String[] args) {
		SpringApplication.run(FocusupApplication.class, args);
	}

}
