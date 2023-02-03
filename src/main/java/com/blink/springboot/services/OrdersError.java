package com.blink.springboot.services;

import com.blink.springboot.entities.Customer;

public class OrdersError extends Error {
	
	public OrdersError(Class<?> clazz, Long id) {
		this(String.format("%s.id: %d not found", clazz.getName(), id));
	}
	
	public OrdersError(String msg) {
		super(msg);
	}
}
