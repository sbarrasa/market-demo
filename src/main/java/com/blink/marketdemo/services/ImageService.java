package com.blink.marketdemo.services;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blink.marketdemo.entities.EntityImage;
import com.blink.mediamanager.Media;
import com.blink.mediamanager.MediaTemplate;


@Service
public class ImageService {
	private static final String defaultSufix = "DEFAULT";

	@Autowired
	MediaTemplate mediaTemplate;
	
	@Autowired
	MediaTemplate mediaTemplate2;

	
	public URL getURL(EntityImage entity, String... sufix) {
		return getURL(entity.getClass(), entity.getId(), sufix);
	}

	public URL getURL(Class<? extends EntityImage> entityImageClass, Object imageId, String... sufix) {
		return getURL(EntityImage.getImageId(entityImageClass, imageId, sufix), mediaTemplate);
	}


	public ResponseEntity<?> getImage(Class<? extends EntityImage> entityImageClass, Object id, String... sufix){
		UrlResource resource = new UrlResource(getURL(entityImageClass, id, sufix));
		if(!resource.exists())
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(resource);
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

	URL getURL2(Class<? extends EntityImage> entityImageClass) {
		return getURL(EntityImage.getImageId(entityImageClass, defaultSufix), mediaTemplate2);
	}

	private static URL getURL(String imageFile, MediaTemplate mediaTemplateActive) {
		return mediaTemplateActive.getURL(imageFile);
	}
	

}
