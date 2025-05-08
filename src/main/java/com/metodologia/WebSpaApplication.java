package com.metodologia;

import or.springframework.context.annotation.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class WebSpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSpaApplication.class, args);
	}

	@Configuration
	public static class MyConfiguration{
		@Bean
		public WebMvcConfigurer corsConfigurer(){
			return new WebMvcConfigurer(){
			@Override
			public void addCorsMapping(CorsRegistry registry){
				registry.addMapping("/**")
					.allowedMethods("HEAD","GET","PUT","POST","DELETE","PATCH");
			}
		};
	}
}
}
