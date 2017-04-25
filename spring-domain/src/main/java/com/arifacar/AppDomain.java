package com.arifacar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppDomain {

	private static final Logger log = LoggerFactory.getLogger(AppDomain.class);

	public static void main(String[] args) {
		SpringApplication.run(AppDomain.class);
	}

}
