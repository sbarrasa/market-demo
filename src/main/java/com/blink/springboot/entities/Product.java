package com.blink.springboot.entities;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.blink.springboot.config.Formats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView({Views.Order.class, Views.ProductUpdate.class})
	private Long id;
	
	@Column(nullable = false, length =  50)
	@JsonView({Views.Order.class, Views.ProductUpdate.class})
	private String name; 
	
	@Column(nullable = true, length =  255)
	@JsonView({Views.Order.class, Views.ProductUpdate.class})
	private String description;
	
	@JsonView({Views.Order.class, Views.ProductUpdate.class})
	private Integer stock;
	
	@JsonView({Views.Order.class, Views.ProductUpdate.class})
	private Double price;
	
	
	@CreationTimestamp
	@JsonFormat(pattern=Formats.DATE_TIME_MILIS)
	private LocalDateTime created;
 
    @UpdateTimestamp
	@JsonFormat(pattern=Formats.DATE_TIME_MILIS)
    private LocalDateTime updated;
	
    

    public Product() {}
   
    public Product(Long id) {
    	setId(id);
    }

    
    public boolean isLoaded() {
    	return id!=null && name!=null;
    }
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
	
	public String toString() {
		return String.format("#%d:%s stock:%d price:%,.2f", getId(), getName(), getStock(), getPrice());
	}
	
}
