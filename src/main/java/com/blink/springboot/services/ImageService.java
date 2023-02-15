package com.blink.springboot.services;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.blink.mediamanager.MediaException;
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
    private static final String defaultSufix = "DEFAULT";

    @Autowired
    MediaTemplate mediaTemplate;

    @Autowired
    MediaTemplate mediaTemplate2;


    public URL getURL(EntityImage entity, String... sufix) {
        return getURL(entity.getClass(), entity.getId(), sufix);
    }


    public URL getURL(Class<? extends EntityImage> entityImageClass, Object imageId, String... sufix) {
        try {
            return getURL(EntityImage.getImageId(entityImageClass, imageId, sufix), mediaTemplate);
        } catch (MediaException e) {
            Logger.getGlobal().warning(e.getMessage());
            return null;
        }
    }


    public ResponseEntity<?> getImage(Class<? extends EntityImage> entityImageClass, Object id, String... sufix) {
        UrlResource resource;
        resource = new UrlResource(getURL(entityImageClass, id, sufix));
        if (!resource.exists())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(resource);
    }

    public Map<Object, URL> getURLs(Collection<? extends EntityImage> entities, String... sufix) {
        Map<Object, URL> urls = new HashMap<>();

        entities.forEach(e -> {
            urls.put(e.getId(), getURL(e, sufix));
        });

        return urls;

    }


    public Collection<Media> upload(Collection<Media> medias) {
        return mediaTemplate.upload(medias);

    }

    private URL getURL2(String imageId, MediaTemplate mediaTemplateActive, Exception e) throws MediaException {
        return mediaTemplate2.getValidURL(imageId);
    }


    private URL getURL(String imageId, MediaTemplate mediaTemplateActive) throws MediaException {
        return mediaTemplateActive.getValidURL(imageId);
    }


}
