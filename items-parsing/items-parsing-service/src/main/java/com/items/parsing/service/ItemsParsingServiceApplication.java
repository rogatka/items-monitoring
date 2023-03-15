package com.items.parsing.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.items.parsing"})
@ConfigurationPropertiesScan
@SpringBootApplication
public class ItemsParsingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemsParsingServiceApplication.class, args);
    }

}
