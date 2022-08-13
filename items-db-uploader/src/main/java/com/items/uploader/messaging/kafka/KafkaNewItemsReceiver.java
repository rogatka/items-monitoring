/*
 * Copyright © 2021 EPAM Systems, Inc. All Rights Reserved. All information contained herein is, and remains the
 * property of EPAM Systems, Inc. and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from EPAM Systems, Inc
 */
package com.items.uploader.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.items.uploader.mapper.ItemMapper;
import com.items.uploader.model.Item;
import com.items.uploader.model.dto.ItemDto;
import com.items.uploader.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaNewItemsReceiver implements KafkaMessageReceiver<String> {

    private final ObjectMapper objectMapper;

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    @KafkaListener(topics = "${spring.kafka.properties.topic.consumer.new-items}", groupId = "${spring.kafka.consumer.group_id}")
    @Override
    public void receive(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Received payload -> {}", consumerRecord.toString());
        String itemJson = consumerRecord.value();
        try {
            ItemDto itemDto = objectMapper.readValue(itemJson, ItemDto.class);
            itemService.findFirstByCodeOrderByCreatedAtDesc(itemDto.getCode())
                    .ifPresentOrElse(
                            existingItem -> updatePriceIfChanged(itemDto.getPrice(), existingItem),
                            () -> itemService.save(itemDto)
                    );
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize ItemDto from JSON -> {}", itemJson);
            throw new RuntimeException(e);
        }
    }

    private void updatePriceIfChanged(BigDecimal price, Item item) {
        Objects.requireNonNull(price);
        if (!price.equals(item.getLastPrice())) {
            log.debug("Price for Item {} has changed. Previously was: {}, now: {}", item, price, item.getLastPrice());
            item.setLastPrice(price);
            item.addPriceHistory(Item.PriceHistoryElem.builder().price(price).build());
            itemService.save(item);
        } else {
            log.debug("Price for Item {} has not changed", item);
        }
    }
}
