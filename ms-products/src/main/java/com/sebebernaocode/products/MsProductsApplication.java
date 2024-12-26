package com.sebebernaocode.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MsProductsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsProductsApplication.class, args);
    }
}