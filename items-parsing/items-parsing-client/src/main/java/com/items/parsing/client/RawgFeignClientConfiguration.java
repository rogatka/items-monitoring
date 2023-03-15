package com.items.parsing.client;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RawgFeignClientConfiguration {

    private static final String API_KEY_HEADER = "key";

    @Value("${parsing.games.rawg.api-key}")
    private String apiKey;

    @Bean
    public Decoder feignDecoder() {
        ObjectFactory<HttpMessageConverters> messageConverters = () -> {
            HttpMessageConverters converters = new HttpMessageConverters();
            return converters;
        };
        return new SpringDecoder(messageConverters);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.query(API_KEY_HEADER, apiKey);
            requestTemplate.uri(
            requestTemplate.request()
                    .url()
                    .replace("%2C", ",")
            );
        };
    }
}