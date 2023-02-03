package com.blink.springboot.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.blink.mediamanager.MediaError;
import com.blink.mediamanager.MediaTemplate;
import com.blink.mediamanager.local.MediaLocal;

@Configuration
public class MediaConfig {
	@Autowired
    private ApplicationContext applicationContext;
	
	@SuppressWarnings("static-method")
	@Bean
	public MediaLocal localMedia() {
		return new MediaLocal();
	}
	
	
	
	@Bean 
	public MediaTemplate mediaTemplate(@Value("${com.blink.mediamanager.class}") String className) {
		try {
			
			return (MediaTemplate) applicationContext.getBean(Class.forName(className));
			
		} catch (Exception e) {
			throw new MediaError(String.format("Error when trying to instantiate MediaTemplate class %s", className));
		}
	}
	
	
	
	

}
