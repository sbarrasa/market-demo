package com.blink.marketdemo.entities;

public interface EntityImage {
	public Object getId();
	
	static public String getImageId(Class<?> clazz, Object id) {
		return getImageId(clazz, id, null);
	}
		
	static public String getImageId(Class<?> clazz, Object id, String sufix) {
		if(sufix == null)
			sufix = "";
		
		return clazz.getSimpleName()+"-"+id+sufix;
	}
	
	
	default public String getImageId() {
		return getImageId(null);
	}
		
	default public String getImageId(String sufix) {
		return getImageId(getClass(), getId(), sufix);
	}
	
}
