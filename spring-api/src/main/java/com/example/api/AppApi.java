package com.example.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class AppApi {

    private static final Logger log = LoggerFactory.getLogger(AppApi.class);

    public static void main(String[] args) {

        SpringApplication.run(AppApi.class);

    }

}
