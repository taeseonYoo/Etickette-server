package com.tae.Etickette;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class EticketteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EticketteApplication.class, args);
	}

}
