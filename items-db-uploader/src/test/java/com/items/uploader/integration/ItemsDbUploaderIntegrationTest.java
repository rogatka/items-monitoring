package com.items.uploader.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.items.uploader.model.Game;
import com.items.uploader.model.Item;
import com.items.uploader.model.SearchIndex;
import com.items.uploader.model.dto.GameDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class ItemsDbUploaderIntegrationTest extends BaseIntegrationTest {

    @Value("${spring.kafka.properties.topic.consumer.new-items.games}")
    private String gamesTopicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @SneakyThrows
    @Test
    void shouldSaveAndIndexGameItem_WhenConsumedMessage() {
        // given
        GameDto gameDto = new GameDto();
        gameDto.setId(123L);
        gameDto.setName("Dead Space");
        gameDto.setReleased("2023-01-01");
        gameDto.setMetacritic(90);
        String itemJson = objectMapper.writeValueAsString(gameDto);

        // when
        kafkaTemplate.send(gamesTopicName, UUID.randomUUID().toString(), itemJson);

        // then
        Query query = new Query(Criteria
                .where(Game.Fields.platformId).is(gameDto.getId())
                .and(Game.Fields.name).is(gameDto.getName())
                .and(Game.Fields.releaseDate).is(gameDto.getReleased())
                .and(Game.Fields.lastRating).is(gameDto.getMetacritic()));

        AtomicReference<List<Game>> gamesAtomicReference = waitUntilMessageIsReceivedAndGetResult(query);

        List<Game> games = gamesAtomicReference.get();
        assertThat(games)
                .isNotNull()
                .hasSize(1)
                .first()
                .isNotNull()
                .returns(gameDto.getId(), Game::getPlatformId)
                .returns(gameDto.getName(), Game::getName)
                .returns(gameDto.getMetacritic(), Game::getLastRating)
                .returns(gameDto.getReleased(), Game::getReleaseDate)
                .hasNoNullFieldsOrPropertiesExcept(Game.Fields.ratingHistory);

        awaitUntilIndexIsCreated();

        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(
                org.springframework.data.elasticsearch.core.query.Criteria
                        .where(Game.Fields.platformId).is(gameDto.getId())
                        .and(Game.Fields.name).is(gameDto.getName())
                        .and(Game.Fields.releaseDate).is(gameDto.getReleased())
                        .and(Game.Fields.lastRating).is(gameDto.getMetacritic()))
                .build();

        SearchHits<Item> searchHits = elasticsearchOperations.search(criteriaQuery, Item.class, IndexCoordinates.of(SearchIndex.GAMES_INDEX.getIndexName()));

        assertThat(searchHits)
                .isNotNull()
                .hasSize(1);

        SearchHit<Item> searchHit = searchHits.getSearchHits().get(0);
        assertThat(searchHit)
                .isNotNull()
                .extracting(SearchHit::getContent)
                .isNotNull();

        assertThat((Game)searchHit.getContent())
                .returns(gameDto.getId(), Game::getPlatformId)
                .returns(gameDto.getName(), Game::getName)
                .returns(gameDto.getMetacritic(), Game::getLastRating)
                .returns(gameDto.getReleased(), Game::getReleaseDate)
                .hasNoNullFieldsOrPropertiesExcept(Item.Fields.createdAt, Item.Fields.updatedAt, Game.Fields.ratingHistory);
    }

    private void awaitUntilIndexIsCreated() {
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                        .until(() -> elasticsearchOperations
                                .indexOps(IndexCoordinates.of(SearchIndex.GAMES_INDEX.getIndexName()))
                                .exists());
    }

    private AtomicReference<List<Game>> waitUntilMessageIsReceivedAndGetResult(Query query) {
        AtomicReference<List<Game>> gamesAtomicReference = new AtomicReference<>();
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollDelay(1, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    List<Game> gamesResult = mongoTemplate.find(query, Game.class);
                    if (gamesResult.isEmpty()) {
                        return false;
                    }
                    gamesAtomicReference.set(gamesResult);
                    return true;
                });
        return gamesAtomicReference;
    }
}
