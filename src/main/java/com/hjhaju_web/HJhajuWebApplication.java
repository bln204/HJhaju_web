package com.hjhaju_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
@SpringBootApplication
public class HJhajuWebApplication {

	public static void main(String[] args) {


		SpringApplication.run(HJhajuWebApplication.class, args);
	}

}
