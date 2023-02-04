package com.blink.springboot.entities;

public interface EntityImage {
	public Object getId();
	
	static public String getImageId(Class<?> clazz, Object id, String... sufixes) {
		String sufix = "";
		if(sufixes.length >0)
			sufix = sufixes[0];

		return clazz.getSimpleName()+"-"+id+sufix;
	}
	
	
	default public String getImageId(String... sufixes) {
		return getImageId(getClass(), getId(), sufixes);
	}
	
}
