package com.blink.springboot;

import java.util.Properties;
import java.util.logging.Logger;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableCaching
@EnableWebMvc
@SpringBootApplication
public class MarketDemoApplication extends SpringApplication{
	public static void main(String[] args) {
		new SpringApplicationBuilder(MarketDemoApplication.class)
			.properties(staticProperties())
			.run(args);
	}
	
	
	private static Properties staticProperties() {
		Properties properties = new Properties();
		properties.put("spring.application.name","Market DEMO");
		properties.put("spring.application.version","1.2");

		return properties;
	}

	@Bean
	public RegistryEventConsumer<CircuitBreaker> myRegistryEventConsumer() {

		return new RegistryEventConsumer<CircuitBreaker>() {
			@Override
			public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
				entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> Logger.getGlobal().info(event.toString()));
			}

			@Override
			public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {

			}

			@Override
			public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {

			}
		};
	}

	@Bean
	public RegistryEventConsumer<Retry> myRetryRegistryEventConsumer() {

		return new RegistryEventConsumer<Retry>() {
			@Override
			public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
				entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> Logger.getGlobal().info(event.toString()));
			}

			@Override
			public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {

			}

			@Override
			public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {

			}
		};
	}



}