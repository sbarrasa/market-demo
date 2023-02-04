package com.blink.springboot.entities;

//TODO: mover a MediaManager
//TODO: resolver Static getImageId via reflection 
public interface EntityImage {
	public Object getId();
	
	static public String getImageId(Class<?> clazz, Object id) {
		return clazz.getSimpleName()+"-"+id;
	}

	static public String getImageId(Class<?> clazz, Object id, String sufix) {
		return getImageId(clazz, id)+sufix;
	}
	
	default public String getImageId() {
		return getImageId(getClass(), getId());
	}
	
	default public String getImageId(String sufix) {
		return getImageId()+sufix;
	}
}
