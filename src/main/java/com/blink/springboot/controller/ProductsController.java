package com.blink.springboot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blink.springboot.dao.ProductsRepository;
import com.blink.springboot.entities.Product;
import com.blink.springboot.entities.Views;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/products")
public class ProductsController {
	@Autowired
	private ProductsRepository productsRepository;

	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public Page<Product> getAll(@RequestParam(required = false) Optional<Integer> page,
			 				  @RequestParam(required = false) Optional<Integer> size) {
		
	
		
		return productsRepository.findAll(PageRequest.of( 
										page.orElse(0), 
										size.orElse(50),
										Sort.by("id")));

		
	}
	
	@GetMapping("/{id}")
	public Product getById(@PathVariable Long id) {
		return productsRepository.findById(id).orElseThrow();
	}
	
	@RequestMapping(path = "/batch", method=RequestMethod.POST)
	public List<Product> saveBatch(@JsonView(Views.ProductUpdate.class) 
									@RequestBody List<Product> products) {
		return productsRepository.updateAll(products);
	}

	@RequestMapping(path = "/", method=RequestMethod.POST)
	public Product save(@JsonView(Views.ProductUpdate.class)
							@RequestBody Product productUpdate) {

		return productsRepository.update(productUpdate);
	}

	
	@RequestMapping(path = "/{id}", method=RequestMethod.DELETE)
	public Product delete(@PathVariable Long id) {
		Product product = productsRepository.findById(id).orElseThrow();
		
		productsRepository.delete(product);
		
		return product;
		
	}
	

}
