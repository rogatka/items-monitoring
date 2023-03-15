package com.items.parsing.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = RawgFeignClient.class)
public class RawgClientAutoConfiguration {
}
