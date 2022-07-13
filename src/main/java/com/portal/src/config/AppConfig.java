package com.portal.src.config;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class AppConfig {
	
	@Bean
	public Docket demoApi() {
		return new Docket(SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build();
	}
}
