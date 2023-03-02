package com.blink.marketdemo.config;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.blink.mediamanager.MediaError;
import com.blink.mediamanager.MediaTemplate;
import com.blink.mediamanager.local.MediaLocal;
import com.blink.mediamanager.s3.MediaS3;

@Configuration
@EnableAsync
public class MediaConfig {
	
	@Autowired
    private ApplicationContext applicationContext;
	
	@Autowired
	private ServerProperties serverProperties;
	
	
	@Bean
	public MediaLocal mediaLocal() {
		MediaLocal mediaLocal = new MediaLocal();
		mediaLocal.setPort(serverProperties.getPort());
		return mediaLocal;
	}
	
	@SuppressWarnings("static-method")
	@Bean
    public MediaS3 s3(@Value("${aws.access.key.id}") String accessKey,
    	    @Value("${aws.secret.access.key}") String secretKey,
    	    @Value("${aws.s3.region}") String region,
    	    @Value("${aws.s3.bucket.name}") String bucket) {

		return new MediaS3()
					.setAccessKey(accessKey)
					.setSecretKey(secretKey)
					.setBucket(bucket)
					.setRegion(region);
		
		}
	
	
		
	@Bean 
	public MediaTemplate mediaTemplate(@Value("${com.blink.mediamanager.class}") String className,
										@Value("${com.blink.mediamanager.path}") String path)  {
		try {
			MediaTemplate mediaTemplate ;
			try {
				
				mediaTemplate = (MediaTemplate) applicationContext.getBean(Class.forName(className));
			}catch (NoSuchBeanDefinitionException eb) {
				mediaTemplate = MediaTemplate.buildMediaTemplate(className);
			}
			
			mediaTemplate.setPath(path);
			return mediaTemplate;
		} catch (Exception e) {
			throw new MediaError(String.format("Error when trying to instantiate MediaTemplate class %s", className));
		}
	}
	
	
	

}
