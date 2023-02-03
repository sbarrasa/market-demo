package com.blink.springboot.controller;


import java.util.List;
import java.util.Optional;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.blink.mediamanager.MediaTemplate;
import com.blink.springboot.entities.Customer;
import com.blink.springboot.entities.Sex;
import com.blink.springboot.services.CustomersService;

@RestController
@RequestMapping("/customers")
public class CustomersController {
	@Autowired
	MediaTemplate mediaTemplate;
	
	
	@Autowired
	private CustomersService customersManager;


	@GetMapping("/view/{id}")
	public ModelAndView view(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView();

	    mav.addObject("customer", customersManager.get(id));
	    mav.setViewName("customer");

        return mav;
	}
	
	@GetMapping(value = "/view/")
	public ModelAndView view(@RequestParam(required = false) List<String> orderBy) {
		
		ModelAndView mav = new ModelAndView();
		
  	  	mav.setViewName("customers");

		List<Customer> customers = customersManager.getAll(orderBy);
		
		Customer prueba = customers.get(new Random().nextInt(customers.size()-1));
		Integer money = new Random().nextInt(2000)-1000;
		mav.addObject( "prueba", prueba);
		mav.addObject( "money", money);
		
		mav.addObject("customers", customers);
	  
        return mav;
	}
	
	

	
	
	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public Page<Customer> getAll(@RequestParam(required = false) Optional<Integer> page,
			 				  @RequestParam(required = false) Optional<Integer> size,
			 				  @RequestParam(required = false) List<String> orderFields) {
		
		
		return customersManager.getPaginated(page, size, orderFields);
		
			
	}

	@GetMapping("/")
	public List<Customer> get(@RequestParam(required = false) String lastNames, 
							  @RequestParam(required = false) String names,
							  @RequestParam(required = false) Sex sex,
							  @RequestParam(required = false) Optional<Integer> ageFrom,
							  @RequestParam(required = false) Optional<Integer> ageTo) {
		
		return customersManager.get(lastNames, names, sex, 
										Range.closed(ageFrom.orElse(0),
													 ageTo.orElse(Integer.MAX_VALUE)));
		
		
	}

	@GetMapping("/{id}")
	public Customer getById(@PathVariable Long id) {
		return customersManager.get(id);
	}

	@PostMapping("/")
	public Customer create(@RequestBody Customer customer) {
		return customersManager.save(customer)
;
	}

	@PutMapping("/{id}")
	public Customer update(@PathVariable Long id, @RequestBody Customer customer){
		customer.setId(id);
		
		return customersManager.save(customer);
		
	}

	@DeleteMapping("/{id}")
	public Customer delete(@PathVariable Long id){
		return customersManager.delete(id);
		
	}
		


	
}