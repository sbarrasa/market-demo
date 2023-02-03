package com.blink.springboot;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Application extends SpringApplication{
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
			.properties(staticProperties())
			.run(args);
	}
	
	
	private static Properties staticProperties() {
		Properties properties = new Properties();
		
		properties.put("spring.application.name","Market DEMO");
		properties.put("spring.application.version","1.2");

		return properties;
	}


}