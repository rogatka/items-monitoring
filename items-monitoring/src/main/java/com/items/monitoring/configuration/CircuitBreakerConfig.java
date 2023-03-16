package com.items.monitoring.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CircuitBreakerConfig {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    void customizeCircuitBreakerEventEmitter() {
        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(circuitBreaker -> circuitBreaker.getEventPublisher().onEvent(
                        (event) -> log.info(event.toString())
                ));
        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(circuitBreaker -> circuitBreaker.getEventPublisher().onStateTransition(
                        (event) -> log.info(event.toString())
                ));
    }

}

