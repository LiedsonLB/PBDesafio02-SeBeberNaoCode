package com.sebebernaocode.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication()
public class MsProductsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsProductsApplication.class, args);
    }
}