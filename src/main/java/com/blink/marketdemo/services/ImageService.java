package com.blink.marketdemo.services;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blink.marketdemo.entities.EntityImage;
import com.blink.mediamanager.ImageResizer;
import com.blink.mediamanager.Media;
import com.blink.mediamanager.MediaException;
import com.blink.mediamanager.MediaTemplate;


@Service
public class ImageService {
	private static final String DEFAULT_ID = "DEFAULT";
	private static final String DEFAULT_EXTENSION = "jpg";

	@Value("${com.blink.mediamanager.imageresizer.mainwidth}")
	private Integer mainWidth;
	
	@Value("${com.blink.mediamanager.imageresizer.thumbwidth}")
	private Integer thumbWidth;

	
	@Autowired
	MediaTemplate mediaTemplate;

	@Autowired
	MediaTemplate mediaLocal;

	public URL getURL(EntityImage entityImage) {
		return getURL(entityImage, null);
	}

	public URL getURL(EntityImage entityImage, String sufix) {
		return getURL(entityImage.getClass(), entityImage.getId(), sufix);
		
	}

	public URL getURL(Class<? extends EntityImage> entityImageClass, Object id, String sufix) {
		String imageId ;
		imageId = EntityImage.getImageId(entityImageClass, id, sufix);
		
		return mediaTemplate.getURL(imageId);
	}

		
	@CircuitBreaker(name = "imageService", fallbackMethod = "getImageFallback")
	public ResponseEntity<?> getImage(Class<? extends EntityImage> entityImageClass, Object id, String sufix) throws MediaException {
		String imageId  = EntityImage.getImageId(entityImageClass, id, sufix);

		URL url = mediaTemplate.getValidURL(imageId);

		Resource resource = new UrlResource(url);
		
		return ResponseEntity.ok(resource);
     
	} 

	public ResponseEntity<?> getImageFallback(Class<? extends EntityImage> entityImageClass, Object id, String sufix, Exception e)   {
		String imageId = EntityImage.getImageId(entityImageClass, DEFAULT_ID, sufix)+"."+DEFAULT_EXTENSION;
		Resource resource = new ClassPathResource("/static/"+imageId);
		return ResponseEntity.ok(resource);

	}

	public Map<Object, URL> getURLs(Collection<? extends EntityImage> entities, String sufix ) {
		Map<Object, URL> urls = new HashMap<>();
		
		entities.forEach(entityImage -> {
			urls.put(entityImage.getId(), getURL(entityImage, sufix));
		});
		
		return urls;
				
	}
	

	public Collection<Media> upload(Media media) throws MediaException {
		ImageResizer images = new ImageResizer(media)
				.setPrincipalWidth(mainWidth)
				.setThumbnailWidth(thumbWidth);

		return upload(images.getResizes());
	}
	
	public Collection<Media> upload(Collection<Media> medias){
		return mediaTemplate.upload(medias);
	}

}
