package com.blink.springboot.services;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blink.mediamanager.Media;
import com.blink.mediamanager.MediaTemplate;
import com.blink.springboot.entities.EntityImage;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ImageService {
	@Autowired
	MediaTemplate mediaTemplate;
	
	@Autowired
	MediaTemplate mediaTemplate2;
	
	@CircuitBreaker(name = "imageService", fallbackMethod = "altURL")
	public URL getURL(String imageID) {
		return mediaTemplate.getURL(imageID);
	}
	
	public Map<Object, URL> getURLs(Collection<? extends EntityImage> entities, String... sufix ) {
		Map<Object, URL> urls = new HashMap<>();
		
		entities.forEach(e -> {
			urls.put(e.getId(), mediaTemplate.getURL(e.getImageId(sufix)));
		});
		
		return urls;
				
	}
	
	URL getAltURL(String imageID) {
		return mediaTemplate2.getURL(imageID);
	}
	
	public Collection<Media> upload(Collection<Media> medias){
		return 	mediaTemplate.upload(medias);

	}
	
	
	

}
