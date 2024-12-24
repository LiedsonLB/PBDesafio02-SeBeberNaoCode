package com.sebebernaocode.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
public class MsAuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAuthorizationApplication.class, args);
	}

}
