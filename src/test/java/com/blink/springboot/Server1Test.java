package com.blink.springboot;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.blink.springboot.entities.Customer;
import com.blink.springboot.services.CustomersManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class Server1Test {

    @Autowired
    private WebTestClient webClient;

   	@Autowired
	private CustomersManager customersManager;

   	private Long customerIdTest = 1l;

	@Test
	public void testRepopsitory() {
		Assertions.assertEquals(customerIdTest, customersManager.get(customerIdTest).getId());
 	}

	
}
