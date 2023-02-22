package com.blink.marketdemo.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blink.marketdemo.entities.Customer;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Long> {


	
}
