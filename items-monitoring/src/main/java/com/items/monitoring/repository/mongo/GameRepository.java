package com.items.monitoring.repository.mongo;

import com.items.monitoring.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String> {

    Flux<Game> findByNameLike(String query);
}
