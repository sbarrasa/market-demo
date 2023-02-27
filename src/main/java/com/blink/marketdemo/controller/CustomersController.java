package com.blink.marketdemo.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.blink.marketdemo.entities.Customer;
import com.blink.marketdemo.entities.Sex;
import com.blink.marketdemo.services.CustomersService;
import com.blink.marketdemo.services.ImageService;
import com.blink.mediamanager.ImageResizer;
import com.blink.mediamanager.Media;
import com.blink.mediamanager.MediaException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/customers")
public class CustomersController {
	
	
	@Autowired
	ImageService imageService;
	

	@Autowired
	private CustomersService customersService;

	@GetMapping("{id}/view")
	public ModelAndView view(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView();
		Customer customer = customersService.get(id);
		
		mav.setViewName("customer");
		
		mav.addObject("customer", customer);
		mav.addObject("avatar", imageService.getURL(customer));

		
		return mav;
	}

	@GetMapping(value = "/view")
	public ModelAndView view(@RequestParam(required = false) List<String> orderBy) {

		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("customers");

		List<Customer> customers = customersService.getAll(orderBy);

		mav.addObject("customers", customers);
		mav.addObject("imageURLs", imageService.getURLs(customers, ImageResizer.ID_THUMBNAIL));

		return mav;
	}

	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public Page<Customer> getAll(@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(required = false) Optional<Integer> size,
			@RequestParam(required = false) List<String> orderFields) {

		return customersService.getPaginated(page, size, orderFields);

	}

	@GetMapping("/")
	public List<Customer> get(@RequestParam(required = false) String lastNames,
			@RequestParam(required = false) String names, @RequestParam(required = false) Sex sex,
			@RequestParam(required = false) Optional<Integer> ageFrom,
			@RequestParam(required = false) Optional<Integer> ageTo) {

		return customersService.get(lastNames, names, sex,
				Range.closed(ageFrom.orElse(0), ageTo.orElse(Integer.MAX_VALUE)));

	}

	@GetMapping("/{id}")
	public Customer getById(@PathVariable Long id) {
		return customersService.get(id);
	}

	@PostMapping("/")
	public Customer create(@RequestBody Customer customer) {
		return customersService.save(customer);
	}

	@PutMapping("/{id}")
	public Customer update(@PathVariable Long id, @RequestBody Customer customer) {
		customer.setId(id);

		return customersService.save(customer);

	}

	@DeleteMapping("/{id}")
	public Customer delete(@PathVariable Long id) {
		return customersService.delete(id);

	}

	@ResponseBody
	@RequestMapping(path = "/{id}/image/upload", 
					method = RequestMethod.POST, 
					consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Collection<Media> uploadImage(@PathVariable Long id, @RequestPart() MultipartFile multipartFile)
			throws IOException, MediaException {
		Media media = new Media()
							.setId(Customer.getImageId(id))
							.setStream(multipartFile.getInputStream())
							.setContentType(multipartFile.getContentType());
		
		return imageService.upload(media);
		
	}

	@GetMapping(value=("/{id}/image"), produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<?> getImage(@PathVariable Long id) throws MediaException {
		return imageService.getImage(Customer.class, id);
	}

	@GetMapping(value=("/{id}/thumbnail"), produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<?> getThumbnail(@PathVariable Long id) throws MediaException {
		return imageService.getImage(Customer.class, id, ImageResizer.ID_THUMBNAIL);
	}
	


}