package com.example.secure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories("com.example.secure.service")
public class SpSecureApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpSecureApplication.class, args);
	}

}
