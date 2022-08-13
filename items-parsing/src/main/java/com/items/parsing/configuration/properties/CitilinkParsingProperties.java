package com.items.parsing.configuration.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ToString
@EqualsAndHashCode
@ConstructorBinding
@ConfigurationProperties(prefix = "parsing.citilink")
public class CitilinkParsingProperties {

    private final String baseUrl;

    public CitilinkParsingProperties(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
