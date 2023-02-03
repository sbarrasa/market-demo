package com.blink.springboot.entities;

public interface EntityImage {
	public Object getId();
	
	default public String getImageId() {
		return getClass().getSimpleName()+"_"+getId();
	}
	
}
