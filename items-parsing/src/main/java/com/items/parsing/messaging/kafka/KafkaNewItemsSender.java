/*
 * Copyright © 2021 EPAM Systems, Inc. All Rights Reserved. All information contained herein is, and remains the
 * property of EPAM Systems, Inc. and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from EPAM Systems, Inc
 */
package com.items.parsing.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.items.parsing.parsers.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaNewItemsSender implements KafkaMessageSender<ItemDto> {

    private final ObjectMapper objectMapper;

    private final String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaNewItemsSender(ObjectMapper objectMapper,
                               @Value("${spring.kafka.properties.topic.producer.new-items}") String topicName,
                               KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(ItemDto itemDto, String key) {
        try {
            String itemJson = objectMapper.writeValueAsString(itemDto);
            log.info("Sending ItemInfo -> {}", itemJson);
            kafkaTemplate.send(topicName, key, itemJson)
                    .addCallback(
                            result -> log.info("ItemInfo has been successfully sent -> {}", itemDto),
                            ex -> log.error("Error during sending ItemInfo ({}) -> {}", itemDto, ex.getMessage())
                    );
        } catch (JsonProcessingException e) {
            log.error("Cannot serialize ItemInfo -> {}", itemDto);
            throw new RuntimeException(e);
        }
    }
}
