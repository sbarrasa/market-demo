package com.blink.springboot.controller;


import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blink.springboot.entities.Views;
import com.blink.springboot.entities.ProductOrdered;
import com.blink.springboot.entities.Order;
import com.blink.springboot.services.OrdersManager;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/orders")
public class OrdersController {
	@Autowired 
	private OrdersManager ordersManager;
	
			
	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public Page<Order> getAll(@RequestParam(required = false) Optional<Integer> page,
			 				  @RequestParam(required = false) Optional<Integer> size) {
		
//	return ordersManager.ordersRepository.findAll();	
		return ordersManager.ordersRepository.findAll(PageRequest.of( 
										page.orElse(0), 
										size.orElse(50),
										Sort.by("updated")));

		
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	@JsonView(Views.Order.class)
	public ResponseEntity<Order> getById(@PathVariable Long id) {
		Order order = ordersManager.ordersRepository.findById(id)
				.orElseThrow();
		return ResponseEntity.ok(order);
	}
	
	@RequestMapping(path = "/", method = RequestMethod.POST)
	@JsonView(Views.Order.class)
	@Transactional
	public Order create(@RequestParam Long customerId,
						@JsonView(Views.ProductOrderedRequest.class) 
						@RequestBody Set<ProductOrdered> productsOrderedSimple ) {
		
		return ordersManager.save(customerId, productsOrderedSimple);
		
			
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	@JsonView(Views.Order.class)
	@Transactional
	public Order update(@PathVariable Long id, @RequestBody Order orderUpdate){
		orderUpdate.setId(id);
		return ordersManager.ordersRepository.save(orderUpdate);
	}
	
	@JsonView(Views.Order.class)
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Order> delete(@PathVariable Long orderId){
		Order order = ordersManager.delete(orderId);
		
		return ResponseEntity.ok(order);
	}
	
		
}