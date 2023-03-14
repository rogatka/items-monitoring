package com.items.monitoring.repository.mongo.custom.impl;

import com.items.monitoring.model.Game;
import com.items.monitoring.repository.mongo.GameRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Repository
@RequiredArgsConstructor
public class GameRepositoryCustomImpl implements GameRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Game> findByReleaseDateAfter(LocalDate date, Sort sort) {
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        Query query = new Query();
        query.addCriteria(Criteria.where("releaseDate").gt(dateString));
        query.with(sort);

        return mongoTemplate.find(query, Game.class);
    }
}
