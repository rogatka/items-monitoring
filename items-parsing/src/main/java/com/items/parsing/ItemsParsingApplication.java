package com.items.parsing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class ItemsParsingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemsParsingApplication.class, args);
    }

}
