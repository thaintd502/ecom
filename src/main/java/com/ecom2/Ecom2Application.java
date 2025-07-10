package com.ecom2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Ecom2Application{

	public static void main(String[] args) {
		SpringApplication.run(Ecom2Application.class, args);
	}

}
