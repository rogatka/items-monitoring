package com.items.parsing.service.configuration;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

    private static final int MAX_MEMORY_SIZE = 16 * 1024 * 1024; // 16 MB

    @Bean
    public WebClient webClient() {
        HttpClient client = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(this::acceptedCodecs)
                .build();
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .exchangeStrategies(strategies)
                .baseUrl("http://localhost:8080")
                .build();
    }

    private void acceptedCodecs(ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.defaultCodecs().maxInMemorySize(MAX_MEMORY_SIZE);
    }
}
