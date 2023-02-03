package com.blink.springboot.entities;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.blink.springboot.config.Formats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.Order.class)
	private Long id;
	
	@ManyToOne
	@JsonView(Views.Order.class)
	private Customer customer;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
    @OrderBy("product_id ASC")
	@JsonView(Views.Order.class)
	private Set<ProductOrdered> productsOrdered ;

	@CreationTimestamp
	@JsonFormat(pattern=Formats.DATE_TIME_MILIS)
	@JsonView(Views.Order.class)
	private LocalDateTime created;
 
    @UpdateTimestamp
	@JsonFormat(pattern=Formats.DATE_TIME_MILIS)
	@JsonView(Views.Order.class)
    private LocalDateTime updated;

    public Order() {}

    public Order(Customer customer, Set<ProductOrdered> productsOrdered) {
    	setCustomer(customer);
    	setProductsOrdered(productsOrdered);
    }
    

	public Customer getCustomer() {
		return customer;
	}
	
	
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
		
	}
	

	public Integer getCntItems() {
		return productsOrdered.stream().mapToInt(product -> product.getCnt()).sum();
	}

	public Double getTotalPrice() { 
		return productsOrdered.stream().mapToDouble(product -> product.getPrice()* product.getCnt()).sum() ;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Set<ProductOrdered> getProductsOrdered() {
		return productsOrdered;
	}

	public void setProductsOrdered(Set<ProductOrdered> productsOrdered) {
		this.productsOrdered = productsOrdered;
		productsOrdered.forEach(p -> p.setOrder(this));
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

}
