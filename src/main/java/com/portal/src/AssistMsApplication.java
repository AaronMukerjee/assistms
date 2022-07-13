package com.portal.src;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
public class AssistMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssistMsApplication.class, args);
	}

}
