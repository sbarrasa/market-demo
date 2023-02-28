package com.blink.marketdemo.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.blink.marketdemo.dao.ProductsRepository;
import com.blink.marketdemo.entities.Product;
import com.blink.marketdemo.entities.Views;
import com.blink.marketdemo.services.ImageService;
import com.blink.mediamanager.ImageResizer;
import com.blink.mediamanager.Media;
import com.blink.mediamanager.MediaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/products")
public class ProductsController {


	@Autowired
	ImageService imageService;

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

	@RequestMapping(path = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Optional<Integer> page,
							   @RequestParam(required = false) Optional<Integer> size) {

		ModelAndView mav = new ModelAndView();

		mav.setViewName("products");

		Page<Product> products = productsRepository.findAll(PageRequest.of(
				page.orElse(0),
				size.orElse(50),
				Sort.by("id")));

		mav.addObject("products", products.getContent());
		mav.addObject("imageURLs", imageService.getURLs(products.getContent(), ImageResizer.ID_THUMBNAIL));

		return mav;
	}

	@GetMapping("{id}/view")
	public ModelAndView view(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView();

		Product product = productsRepository.findById(id).orElseThrow();
		mav.addObject("product", product);
		mav.addObject("image", imageService.getURL(product));

		mav.setViewName("product");

		return mav;
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

	@ResponseBody
	@RequestMapping(path = "/{id}/image/upload",
			method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Collection<Media> uploadImage(@PathVariable Long id, @RequestPart() MultipartFile multipartFile)
			throws IOException, MediaException {
		Media media = new Media()
				.setId(Product.getImageId(id))
				.setStream(multipartFile.getInputStream())
				.setContentType(multipartFile.getContentType());


		return imageService.upload(media);

		
	}

	@GetMapping(value=("/{id}/image"), produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<?> getImage(@PathVariable Long id) throws MediaException {
		return imageService.getImage(Product.class, id, null);
	}

	@GetMapping(value=("/{id}/thumbnail"), produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<?> getThumbnail(@PathVariable Long id) throws MediaException {
		return imageService.getImage(Product.class, id, ImageResizer.ID_THUMBNAIL);
	}




}
