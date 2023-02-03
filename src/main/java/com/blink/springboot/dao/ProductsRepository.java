package com.blink.springboot.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blink.springboot.entities.Product;

@Repository
public interface  ProductsRepository extends JpaRepository<Product, Long>{
	default public Product update(Product productUpdate) {
		Product product = findById(productUpdate.getId()).orElse(productUpdate);
		
		if(productUpdate.getName()!=null)
			product.setName(productUpdate.getName());
	
		if(productUpdate.getDescription()!=null)
			product.setDescription(productUpdate.getDescription());
		
		if(productUpdate.getPrice()!=null)
			product.setPrice(productUpdate.getPrice());
	
		if(productUpdate.getStock()!=null)
			product.setStock(productUpdate.getStock());
	
				
		product.setUpdated(productUpdate.getUpdated());
		
		return save(product);
		
	}

	default public List<Product> updateAll(List<Product> products){
		return products.stream().map(p -> update(p)).collect(Collectors.toList());
	}
	
	
	
}
