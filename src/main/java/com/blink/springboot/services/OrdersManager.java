package com.blink.springboot.services;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blink.springboot.dao.CustomersRepository;
import com.blink.springboot.dao.OrdersRepository;
import com.blink.springboot.dao.ProductsRepository;
import com.blink.springboot.entities.Customer;
import com.blink.springboot.entities.Order;
import com.blink.springboot.entities.Product;
import com.blink.springboot.entities.ProductOrdered;

@Service
public class OrdersManager {
	@Autowired
	public OrdersRepository ordersRepository;

	@Autowired
	public CustomersRepository customersRepository;
	
	@Autowired
	public ProductsRepository productsRepository;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public Order save(Long customerId, Set<ProductOrdered> productsOrdered) {
		logger.info("Saving order");
		
		Customer customer = customersRepository.findById(customerId)
				.orElseThrow(() -> new OrdersError(Customer.class, customerId));

		logger.info("Cutomer {} loaded", customer);

		List<Long> productIds = ProductOrdered.getIds(productsOrdered);
		logger.info("Binding products {}", productIds);

		List<Product> products = productsRepository.findAllById(productIds );
		
		ProductOrdered.loadProducts(productsOrdered, products);
	
		productsOrdered.forEach(po -> {
			Product product = po.getProduct();
			
			if(!product.isLoaded() )
				throw new OrdersError(String.format("Product.id: {} doesn't exist", product.getId()));
			
			if(product.getStock() != null && po.getCnt() > product.getStock())
				throw new OrdersError(String.format("There are no suficient stock for product.id: {} (max: {})",
										product.getId(),
										product.getStock())
									 );

			
			product.setStock(product.getStock()-po.getCnt());
			
		});
		
		
		Order order = new Order(customer, productsOrdered);

		logger.info("Updating stock {}", products);

		productsRepository.saveAll(products);
		
		ordersRepository.save(order);
		
		logger.info("Order #{} saved", order.getId());
		
		return order ;
	}

	public Order delete(Long orderId) {
		Order order = ordersRepository.findById(orderId)
				.orElseThrow();
		
		ordersRepository.delete(order);
		
		return order;
	}
	
}
