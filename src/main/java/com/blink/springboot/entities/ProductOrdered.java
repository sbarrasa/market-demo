package com.blink.springboot.entities;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;



@Entity
@Table(name = "orders_products")
public class ProductOrdered implements Serializable {
	@Id
    @ManyToOne
	@JsonBackReference
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = true, updatable =true)
	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Id
	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	@JsonView(Views.Order.class)
	private Product product;
	
	@JsonView({Views.Order.class, Views.ProductOrderedRequest.class})
	private Double price=0.0;
	
	@JsonView({Views.Order.class, Views.ProductOrderedRequest.class})
	private Integer cnt=0;
	
	@JsonView(Views.ProductOrderedRequest.class)
	public Long getProductId() {
		return getProduct().getId();
	}
	
	@JsonView(Views.ProductOrderedRequest.class)
	public void setProductId(Long productId) {
		getProduct().setId(productId);
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public Product getProduct() {
		if(product== null)
			product = new Product();
		return product;
	}

	
	public Double getPrice() {
		return price;
	}

	public Integer getCnt() {
		return cnt;
	}
	
	public void setPrice(Double price) {
		this.price=price;
	}

	public void setCnt(Integer cnt) {
		this.cnt=cnt;
	}

	public static List<Long> getIds(Set<ProductOrdered> products) {
		return products.stream()
				.map(ProductOrdered::getProductId)
				.collect(Collectors.toList());
	}

	public static Set<ProductOrdered> loadProducts(Set<ProductOrdered> productsOrdered, List<Product> products) {
			
		productsOrdered.forEach(po -> {
			po.product = products.stream()
					.filter( p -> p.getId().equals(po.getProductId()))
					.findFirst().orElse(po.getProduct());
		 	
 		});
 		
 		return productsOrdered;
		
	}
}

