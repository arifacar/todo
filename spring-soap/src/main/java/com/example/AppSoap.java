package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppSoap {

	private static final Logger log = LoggerFactory.getLogger(AppSoap.class);

	public static void main(String[] args) {
		SpringApplication.run(AppSoap.class);

	}
}
