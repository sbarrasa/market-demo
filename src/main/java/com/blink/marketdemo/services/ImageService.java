package com.blink.marketdemo.services;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blink.marketdemo.entities.EntityImage;
import com.blink.mediamanager.ImageResizer;
import com.blink.mediamanager.Media;
import com.blink.mediamanager.MediaException;
import com.blink.mediamanager.MediaTemplate;
import com.blink.mediamanager.local.MediaLocal;


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
	
	MediaTemplate mediaTemplate2 = new MediaLocal().setPath("/");

	
	
	public URL getURL(Class<? extends EntityImage> entityImageClass, Object id, String... sufix) {
		String imageId;
		
		try{
			imageId = EntityImage.getImageId(entityImageClass, id, sufix);
			return mediaTemplate.getValidURL(imageId);

		}catch(Exception e) {
			imageId = EntityImage.getImageId(entityImageClass, DEFAULT_ID, sufix)+"."+DEFAULT_EXTENSION;
			return mediaTemplate2.getURL(imageId);

		}
		
	}


	public ResponseEntity<?> getImage(Class<? extends EntityImage> entityImageClass, Object id, String... sufix){
		UrlResource resource = new UrlResource(getURL(entityImageClass, id, sufix));
		if(!resource.exists())
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(resource);
	}

	public Map<Object, URL> getURLs(Collection<? extends EntityImage> entities, String... sufix ) {
		Map<Object, URL> urls = new HashMap<>();
		
		entities.forEach(entityImage -> {
			urls.put(entityImage.getId(), getURL(entityImage, sufix));
		});
		
		return urls;
				
	}
	
	public URL getURL(EntityImage entity, String... sufix) {
		return getURL(entity.getClass(), entity.getId(), sufix);
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
