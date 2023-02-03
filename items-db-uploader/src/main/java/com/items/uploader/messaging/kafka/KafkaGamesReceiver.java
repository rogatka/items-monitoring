package com.items.uploader.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.items.uploader.mapper.GameMapper;
import com.items.uploader.model.Game;
import com.items.uploader.model.SearchIndex;
import com.items.uploader.model.dto.GameDto;
import com.items.uploader.service.ElasticsearchService;
import com.items.uploader.service.GameService;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Observed
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaGamesReceiver {

    private final ObjectMapper objectMapper;

    private final GameService gameService;

    private final GameMapper gameMapper;

    private final ElasticsearchService elasticsearchService;

    @KafkaListener(topics = "${spring.kafka.properties.topic.consumer.new-items.games}", groupId = "${spring.kafka.consumer.group_id}")
    public void receive(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Received payload -> {}", consumerRecord.toString());
        String itemJson = consumerRecord.value();
        try {
            GameDto gameDto = objectMapper.readValue(itemJson, GameDto.class);
            gameService.findFirstByPlatformIdOrderByCreatedAtDesc(gameDto.getId())
                    .ifPresentOrElse(
                            existingItem -> updateRatingIfChanged(gameDto.getMetacritic(), existingItem),
                            () -> {
                                gameService.save(gameDto);
                                elasticsearchService.indexItem(gameMapper.map(gameDto), SearchIndex.GAMES_INDEX);
                            }
                    );
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize ItemDto from JSON -> {}", itemJson);
            throw new RuntimeException(e);
        }
    }

    private void updateRatingIfChanged(Integer rating, Game game) {
        Objects.requireNonNull(rating);
        if (!rating.equals(game.getLastRating())) {
            log.debug("Rating for Item {} has changed. Previously was: {}, now: {}", game, rating, game.getLastRating());
            game.setLastRating(rating);
            game.addRatingHistory(Game.RatingHistoryElem.builder().rating(rating).build());
            gameService.save(game);
            elasticsearchService.indexItem(game, SearchIndex.GAMES_INDEX);
        } else {
            log.debug("Rating for Item {} has not changed", game);
        }
    }
}
