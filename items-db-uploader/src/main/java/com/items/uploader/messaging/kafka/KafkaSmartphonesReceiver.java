package com.items.uploader.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.items.uploader.mapper.SmartphoneMapper;
import com.items.uploader.model.SearchIndex;
import com.items.uploader.model.Smartphone;
import com.items.uploader.model.dto.SmartphoneDto;
import com.items.uploader.service.ElasticsearchService;
import com.items.uploader.service.SmartphoneService;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Observed
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaSmartphonesReceiver {

    private final ObjectMapper objectMapper;

    private final SmartphoneService smartphoneService;

    private final SmartphoneMapper smartphoneMapper;

    private final ElasticsearchService elasticsearchService;

    @KafkaListener(topics = "${spring.kafka.properties.topic.consumer.new-items.smartphones}", groupId = "${spring.kafka.consumer.group_id}")
    public void receive(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Received payload -> {}", consumerRecord.toString());
        String itemJson = consumerRecord.value();
        try {
            SmartphoneDto smartphoneDto = objectMapper.readValue(itemJson, SmartphoneDto.class);
            smartphoneService.findFirstByCodeOrderByCreatedAtDesc(smartphoneDto.getCode())
                    .ifPresentOrElse(
                            existingItem -> updatePriceIfChanged(smartphoneDto.getPrice(), existingItem),
                            () -> {
                                smartphoneService.save(smartphoneDto);
                                elasticsearchService.indexItem(smartphoneMapper.map(smartphoneDto), SearchIndex.SMARTPHONES_INDEX);
                            }
                    );
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize ItemDto from JSON -> {}", itemJson);
            throw new RuntimeException(e);
        }
    }

    private void updatePriceIfChanged(BigDecimal price, Smartphone smartphone) {
        Objects.requireNonNull(price);
        if (!price.equals(smartphone.getLastPrice())) {
            log.debug("Price for Item {} has changed. Previously was: {}, now: {}", smartphone, price, smartphone.getLastPrice());
            smartphone.setLastPrice(price);
            smartphone.addPriceHistory(Smartphone.PriceHistoryElem.builder().price(price).build());
            smartphoneService.save(smartphone);
            elasticsearchService.indexItem(smartphone, SearchIndex.SMARTPHONES_INDEX);
        } else {
            log.debug("Price for Item {} has not changed", smartphone);
        }
    }
}
