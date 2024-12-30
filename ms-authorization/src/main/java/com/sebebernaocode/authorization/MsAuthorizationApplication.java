package com.sebebernaocode.authorization;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MsAuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAuthorizationApplication.class, args);
	}

}
