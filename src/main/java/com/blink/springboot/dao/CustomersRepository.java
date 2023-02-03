package com.blink.springboot.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blink.springboot.entities.Customer;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Long> {


	
}
