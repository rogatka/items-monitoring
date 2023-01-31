package com.items.parsing.configuration.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ToString
@EqualsAndHashCode
@ConfigurationProperties(prefix = "parsing.smartphones.citilink")
public class CitilinkParsingProperties {

    private final String baseUrl;

    @ConstructorBinding
    public CitilinkParsingProperties(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
