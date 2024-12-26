package com.sebebernaocode.ms_notification;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
@EnableRabbit
@EnableDiscoveryClient
public class MsNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsNotificationApplication.class, args);
	}

}
