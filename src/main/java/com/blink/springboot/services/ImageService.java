package com.blink.springboot.services;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
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

	
	public URL getURL(EntityImage entity, String... sufix) {
		return getURL(entity.getImageId(sufix), mediaTemplate);
	}

	@CircuitBreaker(name = "imageService", fallbackMethod = "getURL2")
	public URL getURL(String imageId) {
		return getURL(imageId, mediaTemplate);
	}


	URL getURL2(String imageId) {
		return getURL(imageId, mediaTemplate2);
	}

	private static URL getURL(String imageId, MediaTemplate mediaTemplateActive) {
		return mediaTemplateActive.getURL(imageId);
	}



	public Map<Object, URL> getURLs(Collection<? extends EntityImage> entities, String... sufix ) {
		Map<Object, URL> urls = new HashMap<>();
		
		entities.forEach(e -> {
			urls.put(e.getId(), getURL(e, sufix));
		});
		
		return urls;
				
	}
	
		
	public Collection<Media> upload(Collection<Media> medias){
		return 	mediaTemplate.upload(medias);

	}

	
	public ResponseEntity<?> getImage(Class<? extends EntityImage> entityImageClass, Object id, String... sufix){
		UrlResource resource;
		resource = new UrlResource(getURL(EntityImage.getImageId(entityImageClass, id, sufix)));
		if(!resource.exists())
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(resource);
	}
	
	

}
