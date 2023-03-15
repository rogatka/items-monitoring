package com.items.parsing.service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        KafkaTemplate<String, String> stringStringKafkaTemplate = new KafkaTemplate<>(producerFactory);
        stringStringKafkaTemplate.setObservationEnabled(true);
        return stringStringKafkaTemplate;
    }
}
