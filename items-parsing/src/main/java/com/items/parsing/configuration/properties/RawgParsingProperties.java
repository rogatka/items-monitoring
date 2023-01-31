package com.items.parsing.configuration.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ToString
@EqualsAndHashCode
@ConfigurationProperties(prefix = "parsing.games.rawg")
public class RawgParsingProperties {

    private final String baseUrl;

    private final String apiKey;

    @ConstructorBinding
    public RawgParsingProperties(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }
}
