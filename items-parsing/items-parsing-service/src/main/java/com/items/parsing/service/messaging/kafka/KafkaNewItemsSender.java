package com.items.parsing.service.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.items.parsing.service.parsers.ItemCategory;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.EnumMap;

@Observed
@Slf4j
@Component
public class KafkaNewItemsSender<T> {

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final EnumMap<ItemCategory, String> itemCategoryTopicMap;

    public KafkaNewItemsSender(ObjectMapper objectMapper,
                               @Value("${spring.kafka.properties.topic.producer.new-items.smartphones}") String smartphonesTopicName,
                               @Value("${spring.kafka.properties.topic.producer.new-items.games}") String gamesTopicName,
                               KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;

        this.itemCategoryTopicMap = new EnumMap<>(ItemCategory.class);
        itemCategoryTopicMap.put(ItemCategory.SMARTPHONE, smartphonesTopicName);
        itemCategoryTopicMap.put(ItemCategory.GAME, gamesTopicName);
    }

    public void send(T item, String key, ItemCategory itemCategory) {
        try {
            String itemJson = objectMapper.writeValueAsString(item);
            log.info("Sending ItemInfo -> {}", itemJson);
            kafkaTemplate.send(itemCategoryTopicMap.get(itemCategory), key, itemJson)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Error during sending ItemInfo ({}) -> {}", item, ex.getMessage());
                        } else {
                            log.info("ItemInfo has been successfully sent -> {}", item);
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("Cannot serialize ItemInfo -> {}", item);
            throw new RuntimeException(e);
        }
    }
}
