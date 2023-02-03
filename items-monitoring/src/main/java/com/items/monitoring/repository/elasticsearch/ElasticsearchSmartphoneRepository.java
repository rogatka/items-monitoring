package com.items.monitoring.repository.elasticsearch;

import com.items.monitoring.model.Smartphone;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElasticsearchSmartphoneRepository extends ReactiveElasticsearchRepository<Smartphone, String> {
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name\"], \"fuzziness\": \"AUTO\"}}")
    Flux<Smartphone> findFuzzyByName(String query);

    Flux<Smartphone> findByNameLike(String query, Pageable pageable);
}
