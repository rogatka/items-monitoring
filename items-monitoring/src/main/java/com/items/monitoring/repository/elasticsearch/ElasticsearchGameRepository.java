package com.items.monitoring.repository.elasticsearch;

import com.items.monitoring.model.Game;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@CircuitBreaker(name = "elasticsearchGameRepositoryCircuitBreaker", fallbackMethod = "fallback")
@Repository
public interface ElasticsearchGameRepository extends ReactiveElasticsearchRepository<Game, String> {

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name\"], \"fuzziness\": \"AUTO\"}}")
    Flux<Game> findFuzzyByName(String query);

    Flux<Game> findByNameLike(String query, Pageable pageable);

    default Flux<Game> fallback(String query, Throwable t) {
        return Flux.empty();
    }

    default Flux<Game> fallback(String query, Pageable pageable, Throwable t) {
        return Flux.empty();
    }
}
