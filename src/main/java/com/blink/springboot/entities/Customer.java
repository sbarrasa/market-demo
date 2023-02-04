package com.blink.springboot.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.blink.springboot.config.Formats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;



@Entity
@Table(name = "customers")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})

public class Customer implements Serializable, EntityImage {
	private static final long serialVersionUID = 666L;

	public Customer() {}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonView(Views.Order.class)
	private Long id;

	@JsonView(Views.Order.class)
	private String names;

	@JsonView(Views.Order.class)
	@Column(name = "lastnames")
	private String lastNames;
	
	@JsonView(Views.Order.class)
	@Enumerated(EnumType.STRING)
	private Sex sex;
	
	@JsonFormat(pattern=Formats.DATE_VIEW)
	private LocalDate birthday;

	@Type(type = "jsonb")
    @Column(columnDefinition = "json") 
	private List<Specs> specs;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name="childs")
	private List<Customer> childs;
	 	
	public Long getId() {
		return id;
	}
	
	public Customer setId(Long id) {
		this.id = id;
		return this;

	}

	public String getNames() {
		return names;
	}
	public Customer setNames(String names) {
		this.names = names;
		return this;
	}
	public String getLastNames() {
		return lastNames;
	}
	public Customer setLastNames(String lastNames) {
		this.lastNames = lastNames;
		return this;
	}
	public Sex getSex() {
		return sex;
	}
	public Customer setSex(Sex sex) {
		this.sex = sex;
		return this;

	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public Customer setBirthday(LocalDate birthday) {
		this.birthday = birthday;
		return this;
	}

	public Integer getAge() {
		return Period.between(birthday, LocalDate.now()).getYears();
	}

	public List<Customer> getChilds() {
		return childs;
	}

	public Customer setChilds(List<Customer> childs) {
		this.childs = childs;
		return this;

	}
	public List<Specs> getSpecs() {
		return this.specs;
	}


	public Customer setSpecs(List<Specs> specs) {
		this.specs = specs;
		return this;

	}

	@JsonIgnore
	public String getFullName() {
		return String.format("%s, %s", getLastNames(), getNames());
	}

	@JsonIgnore
	public String getFullNameAndId() {
		return String.format("%s (#%d)", getFullName(), getId());
	}


	@Override
	public String toString() {
		return getFullNameAndId();
	}
	
	@Override
	public boolean equals(Object o) {
	    // self check
	    if (this == o) return true;
	    // null check
	    if (o == null) return false;
	    // type check and cast
	    if (getClass() != o.getClass()) return false;
	    Customer customer = (Customer) o;
	    // field comparison
	    return this.getId().equals(customer.getId())
	        && this.getNames().equals(customer.getNames())
	        && this.getLastNames().equals(customer.getLastNames());
	}
	
}
